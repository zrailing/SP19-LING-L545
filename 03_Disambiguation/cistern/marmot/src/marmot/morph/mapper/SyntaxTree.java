// Copyright 2013 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package marmot.morph.mapper;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class SyntaxTree {
	private static final char SEPARATOR = '\t';
	private List<Node> nodes_;

	public SyntaxTree() {
		nodes_ = new ArrayList<Node>();
	}
	
	public void addNode(Node node) {
		nodes_.add(node);
	}

	public List<Node> getNodes() {
		return nodes_;
	}

	public void write(Writer writer) throws IOException {		
		int index = 1;
		
		for (Node node : nodes_) {
			writer.write(Integer.toString(index));
			writer.write(SEPARATOR);
			writer.write(node.getForm());
			writer.write(SEPARATOR);
			writer.write(node.getLemma());
			writer.write(SEPARATOR);
			assert !node.getMorphTag().toPosString().isEmpty();
			writer.write(node.getMorphTag().toPosString());
			writer.write(SEPARATOR);
			writer.write(node.getMorphTag().toPosString());
			writer.write(SEPARATOR);
			assert !node.getMorphTag().toHumanMorphString().isEmpty();
			writer.write(node.getMorphTag().toHumanMorphString());
			writer.write(SEPARATOR);
			writer.write(Integer.toString(node.getHeadIndex()));
			writer.write(SEPARATOR);
			writer.write(node.getDeprel());
			writer.write('\n');
			index ++;
		}
	}

}
