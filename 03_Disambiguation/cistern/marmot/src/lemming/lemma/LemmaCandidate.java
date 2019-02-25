// Copyright 2015 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package lemming.lemma;

import java.util.List;

import lemming.lemma.toutanova.EditTreeAligner;
import marmot.util.FeatUtil;
import marmot.util.SymbolTable;
import marmot.util.edit.EditTree;
import marmot.util.edit.EditTreeBuilder;

public class LemmaCandidate {

	private double score_;
	private int[] feature_indexes_;
	private short[] lemma_chars_;
	private List<Integer> alignment_;
	private Integer tree_index_;
	
	public void setScore(double score) {
		score_ = score;
	}

	public double getScore() {
		return score_;
	}

	public int[] getFeatureIndexes() {
		return feature_indexes_;
	}

	public void setFeatureIndexes(int[] feature_indexes) {
		feature_indexes_ = feature_indexes;
	}
	
	public short[] getLemmaChars(SymbolTable<Character> char_table, String lemma, boolean insert) {
		if (lemma_chars_ == null) {
			lemma_chars_ = FeatUtil.getCharIndexes(lemma, char_table, insert);
		}
		return lemma_chars_;
	}

	public List<Integer> getAlignment(EditTreeAligner aligner, String form, String lemma) {
		if (alignment_ == null) {
			alignment_ = aligner.align(form, lemma);
		}
		return alignment_;
	}

	public Integer getTreeIndex(EditTreeBuilder builder, String form, String lemma, SymbolTable<EditTree> tree_table, boolean insert) {
		if (tree_index_ == null) {
			EditTree tree = builder.build(form, lemma);
			tree_index_ = tree_table.toIndex(tree, -1, insert);	
		}
		return tree_index_;
	}

}
