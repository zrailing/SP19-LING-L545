// Copyright 2015 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package lemming.lemma;

public class GoldLemmaGenerator implements LemmaCandidateGenerator {
	private static final long serialVersionUID = 1L;

	@Override
	public void addCandidates(LemmaInstance instance, LemmaCandidateSet set) {
		set.clear();
		assert instance.getLemma() != null;
		set.getCandidate(instance.getLemma());
	}

}
