// Copyright 2015 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package lemming.lemma.edit;

import java.util.List;
import java.util.Map;

import lemming.lemma.LemmaCandidateGenerator;
import lemming.lemma.LemmaCandidateSet;
import lemming.lemma.LemmaInstance;
import marmot.util.edit.EditTree;

public class EditTreeGenerator implements LemmaCandidateGenerator {

	private static final long serialVersionUID = 1L;
	private Map<String, List<EditTree>> map_;
	private String unknown_;
	
	public EditTreeGenerator(String unknown, Map<String, List<EditTree>> map) {
		map_ = map;
		unknown_ = unknown;
	}
	
	@Override
	public void addCandidates(LemmaInstance instance, LemmaCandidateSet set) {
		String form = instance.getForm();
		
		String tag = instance.getPosTag();
		
		List<EditTree> trees_ = null;
		if (tag != null) {
			trees_ = map_.get(tag);
		} 
		
		if (trees_ == null) {
			trees_ = map_.get(unknown_);
		}
		
		for (EditTree tree : trees_) {
			String lemma = tree.apply(form, 0, form.length());
			if (lemma != null) {
				set.getCandidate(lemma);
			}
		}
	}
}
