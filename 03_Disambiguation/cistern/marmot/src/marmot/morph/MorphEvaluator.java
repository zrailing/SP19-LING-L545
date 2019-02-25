// Copyright 2013 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package marmot.morph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lemming.lemma.ranker.RankerCandidate;
import marmot.core.Evaluator;
import marmot.core.Options;
import marmot.core.Sequence;
import marmot.core.State;
import marmot.core.Tagger;
import marmot.core.Token;
import marmot.core.lattice.Hypothesis;
import marmot.core.lattice.SequenceViterbiLattice;
import marmot.core.lattice.SumLattice;
import marmot.core.lattice.ViterbiLattice;
import marmot.core.lattice.ZeroOrderSumLattice;
import marmot.core.lattice.ZeroOrderViterbiLattice;
import marmot.morph.cmd.Trainer;
import marmot.morph.io.SentenceReader;
import marmot.util.Copy;



public class MorphEvaluator implements Evaluator {
	
	private Collection<Sequence> sentences_;
	
	public MorphEvaluator(Collection<Sequence> sentences) {
		sentences_ = sentences;
	}

	public static MorphResult eval(Tagger tagger, Sentence sentence) {
		MorphModel model = (MorphModel) tagger.getModel();
		MorphResult result = new MorphResult(tagger);

		for (Token token : sentence) {
			Word word = (Word) token;
			model.addIndexes(word, false);
		}		
		
		result.num_sentences += 1;

		long time = System.currentTimeMillis();
		SumLattice sum_lattice = tagger.getSumLattice(false, sentence);
		result.sum_lattice_time += System.currentTimeMillis() - time;

		List<List<State>> candidates = sum_lattice.getCandidates();

		ViterbiLattice lattice;
		if (sum_lattice instanceof ZeroOrderSumLattice) {
			lattice = new ZeroOrderViterbiLattice(candidates, model.getOptions()
					.getBeamSize(), model.getMarganlizeLemmas());
		} else {
			lattice = new SequenceViterbiLattice(candidates,
					model.getBoundaryState(tagger.getNumLevels() - 1), model.getOptions().getBeamSize(), model.getMarganlizeLemmas());
		}

		for (List<State> states : candidates) {
			result.num_states += states.size();
			result.candidates_length += 1;
		}

		Hypothesis h = lattice.getViterbiSequence();
		result.time += System.currentTimeMillis() - time;

		List<Integer> candidate_indexes = h.getStates();

		List<int[]> expected_indexes = new ArrayList<int[]>();
		for (int index = 0; index < sentence.size(); index++) {
			expected_indexes.add(sentence.get(index).getTagIndexes());
		}

		int rank = getRankOfSequence(tagger, sentence, lattice, candidates);

		if (rank > 0 && rank - 1 < result.rank.length) {
			result.rank[rank - 1]++;
		}

		boolean sentence_error = false;
		for (int index = 0; index < sentence.size(); index++) {
			Word word = (Word) sentence.get(index);
			int form_index = word.getWordFormIndex();
			boolean is_oov = model.isOOV(form_index);

			State state = candidates.get(index).get(candidate_indexes.get(index))
					.getZeroOrderState();
			
			assert state != null;

			boolean token_error = false;

			State run = state;
			for (int level = tagger.getNumLevels() - 1; level >= 0; level--) {
				assert run != null;
				int expected_tag = expected_indexes.get(index)[level];
				int actual_tag = run.getIndex();
				run = run.getSubLevelState();

				boolean is_error = expected_tag != actual_tag;

				if (is_error) {
					sentence_error = true;
					result.token_errors[level] += 1;
					token_error = true;
				}

				if (is_oov && is_error) {
					result.oov_errors[level] += 1;
				}

			}

			assert state != null;
			List<RankerCandidate> lemma_candidates = state.getLemmaCandidates();
			if (lemma_candidates != null) {
				RankerCandidate candidate = RankerCandidate.bestCandidate(lemma_candidates);
				
				String lemma = word.getLemma().toLowerCase();
				assert lemma != null;
				
				String plemma = candidate.getLemma();
				
				if (!lemma.equals(plemma)) {
					result.lemma_errors ++;
					
					if (is_oov) {
						result.lemma_oov_errors ++;
					}
				}
				
				
			}
			
			
			if (token_error) {
				result.morph_errors++;

				if (is_oov) {
					result.morph_oov_errors += 1;
				}

			}

			if (is_oov) {
				result.num_oovs += 1;
			}
			result.num_tokens += 1;
		}

		if (sentence_error)
			result.sentence_errors += 1;

		if (tagger.getGoldIndexes(sentence, candidates) == null) {
			result.num_unreachable_sentences++;
		}
		return result;
	}

	public MorphResult eval(Tagger tagger) {
		MorphResult result = new MorphResult(tagger.getModel(), tagger.getNumLevels());
		for (Sequence sentence : sentences_) {
			result.increment(eval(tagger, (Sentence) sentence));
		}
		return result;
	}

	protected static int getRankOfSequence(Tagger tagger, Sentence sentence,
			ViterbiLattice lattice, List<List<State>> candidates) {
		List<Integer> gold_indexes = tagger.getGoldIndexes(sentence,
				candidates);

		if (gold_indexes == null) {
			return -1;
		}

		int rank = 0;
		boolean found_hypothesis = false;
		for (Hypothesis h : lattice.getNbestSequences()) {
			List<Integer> indexes = h.getStates();
			if (gold_indexes.equals(indexes)) {
				found_hypothesis = true;
				break;
			}
			rank++;
		}

		if (!found_hypothesis) {
			return -1;
		}

		return rank;
	}
	
	public static MorphResult eval(MorphOptions opts, int num_trials, int seed) {
		opts = (MorphOptions) Copy.clone(opts);
		
		long wall_time = System.currentTimeMillis();
		
		MorphResult result = null;
		
		for (int trial = 0; trial < num_trials; trial++) {
			
			opts.setProperty(Options.SEED, Integer.toString(seed + trial));

			Tagger tagger = Trainer.train(opts);
			
			List<Sequence> sentences = new LinkedList<Sequence>();
			for (Sequence sentence : new SentenceReader(opts.getTestFile())) {
				sentences.add(sentence);
			}

			MorphEvaluator e = new MorphEvaluator(sentences);
			MorphResult current_result = e.eval(tagger);

			if (result == null) {
				result = current_result;
			} else {
				result.increment(current_result);	
			}
		}
		
		// Set time including training time
		result.time = (System.currentTimeMillis() - wall_time) / num_trials;
		return result;
	}
}
