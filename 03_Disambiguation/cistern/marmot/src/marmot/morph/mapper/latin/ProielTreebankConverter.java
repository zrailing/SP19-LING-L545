// Copyright 2013 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package marmot.morph.mapper.latin;

import java.io.IOException;
import java.io.Writer;

import marmot.morph.mapper.Node;
import marmot.morph.mapper.SyntaxTree;
import marmot.morph.mapper.SyntaxTreeIterator;
import marmot.util.FileUtils;

public class ProielTreebankConverter {

	
	public static void main(String[] args) throws IOException {	
		ProielTreebankConverter conv = new ProielTreebankConverter();
		conv.convert(args[0], args[1]);
	}

	public void convert(String input_file, String output_file) throws IOException {
		SyntaxTreeIterator iterator = new SyntaxTreeIterator(input_file, 1, 2, 4, 5, 6, 7, false);
		Writer writer = FileUtils.openFileWriter(output_file);
		
		ProielLdtMorphTagMapper mapper = new ProielLdtMorphTagMapper();
		
		while (iterator.hasNext()) {
		
			SyntaxTree tree = iterator.next();
		
			for (Node node : tree.getNodes()) {
				node.setForm(LatMorNormalizer.normalize(node.getForm()));
				node.setLemma(LatMorNormalizer.normalize(node.getLemma()));				
				node.setMorphTag(mapper.convert(node.getPos(), node.getFeats()));
			}
		
			tree.write(writer);
			writer.write('\n');
		}
		writer.close();
	}

}
