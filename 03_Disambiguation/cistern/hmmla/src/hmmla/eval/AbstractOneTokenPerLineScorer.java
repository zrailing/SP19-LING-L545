// Copyright 2014 Thomas Müller
// This file is part of HMMLA, which is licensed under GPLv3.

package hmmla.eval;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOneTokenPerLineScorer implements Scorer {

	@Override
	public List<Double> getScores(String actual, String prediction) {
		List<Double> scores = new ArrayList<Double>();
		
		double score = 0.;
		double number_of_tokens = 0;
		
		try {
			BufferedReader actual_reader = new BufferedReader(new FileReader(actual));
			BufferedReader prediction_reader = new BufferedReader(new FileReader(prediction));
			
			while (actual_reader.ready() && prediction_reader.ready()) {
				
				String actual_line = actual_reader.readLine().trim();
				String prediction_line = prediction_reader.readLine().trim();
				
				if (actual_line.isEmpty() || prediction_line.isEmpty()) {
					
					if (!(actual_line.isEmpty() && prediction_line.isEmpty())) {
						
						actual_reader.close();
						prediction_reader.close();
						
						throw new RuntimeException("Inconsistent files! " + actual_line + " " + prediction_line);
					}
					
					if (number_of_tokens > 0) {
						scores.add(score);
					}
					score = 0.;
					number_of_tokens = 0;
					
					continue;
				}
				
				String[] actual_tokens = actual_line.split("\\s+");
				String[] prediction_tokens = prediction_line.split("\\s+");
				
				score += getScore(actual_tokens, prediction_tokens);
				number_of_tokens += 1;
				
			}
			
			if (actual_reader.ready() || prediction_reader.ready()) {
				actual_reader.close();
				prediction_reader.close();
				throw new RuntimeException("Inconsistent files!");
			}
			
			actual_reader.close();
			prediction_reader.close();

			
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return scores;
		
	}

	@Override
	public void setOption(String option, String value) {
	}
	
	abstract public double getScore(String[] actual_tokens, String[] prediction_tokens);	
	
}
