// Copyright 2015 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package lemming.lemma;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LemmaCandidateSet implements Iterable<Map.Entry<String, LemmaCandidate>>{

	private Map<String, LemmaCandidate> map_;
	
	public LemmaCandidateSet() {
		map_ = new HashMap<>();
	}
	
	public LemmaCandidateSet(LemmaCandidateSet set) {
		map_ = new HashMap<>(set.map_);
	}

	public LemmaCandidate getCandidate(String lemma) {
		LemmaCandidate candidate = map_.get(lemma);

		if (candidate != null) {
			return candidate;
		}
		
		candidate = new LemmaCandidate();
		map_.put(lemma, candidate);
		return candidate;
	}

	@Override
	public Iterator<Entry<String, LemmaCandidate>> iterator() {
		return map_.entrySet().iterator();
	}

	public int size() {
		return map_.size();
	}

	public boolean contains(String lemma) {
		return map_.containsKey(lemma);
	}
	
	public void clear() {
		map_.clear();
	}
	
	@Override
	public String toString() {
		return map_.keySet().toString();
	}

	public void addCandidate(String lemma, LemmaCandidate candidate) {
		map_.put(lemma, candidate);
	}

	public Collection<LemmaCandidate> getCandidates() {
		return map_.values();
	}
	
}
