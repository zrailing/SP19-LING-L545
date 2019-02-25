// Copyright 2015 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package lemming.lemma.toutanova;

import java.util.List;

import lemming.lemma.LemmaInstance;

public class HackyAlignerTrainer implements AlignerTrainer {

	@Override
	public Aligner train(List<LemmaInstance> instances) {
		return new HackyAligner();
	}

}
