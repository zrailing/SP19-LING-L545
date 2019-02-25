// Copyright 2015 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package lemming.test.lemma;

import lemming.lemma.BackupLemmatizerTrainer;
import lemming.lemma.LemmaCandidateGenerator;
import lemming.lemma.LemmaCandidateGeneratorTrainer;
import lemming.lemma.LemmaInstance;
import lemming.lemma.LemmaResult;
import lemming.lemma.SimpleLemmatizerTrainer;
import lemming.lemma.BackupLemmatizerTrainer.BackupLemmatizerTrainerOptions;
import lemming.lemma.edit.EditTreeGeneratorTrainer;
import lemming.lemma.toutanova.ToutanovaTrainer;

import org.junit.Test;

public class GeneratorTest {

	@Test
	public void testGeneratorTrainer() {
		String train = "trn_mod.tsv";
		String dev = "dev.tsv";
		
		LemmaCandidateGeneratorTrainer trainer = new EditTreeGeneratorTrainer();
		testGeneratorTrainer(trainer, train, dev);
		
		trainer = new SimpleLemmatizerTrainer();
		testGeneratorTrainer(trainer, train, dev);
		
		trainer = new BackupLemmatizerTrainer();
		
		BackupLemmatizerTrainerOptions options = (BackupLemmatizerTrainerOptions) trainer.getOptions();
		
		options.setOption(BackupLemmatizerTrainerOptions.LEMMATIZER_TRAINER, SimpleLemmatizerTrainer.class.getName());
		options.setOption(BackupLemmatizerTrainerOptions.BACKUP_TRAINER, ToutanovaTrainer.class.getName());
		
		testGeneratorTrainer(trainer, train, dev);
	}
	
	public void testGeneratorTrainer(LemmaCandidateGeneratorTrainer trainer, String trainfile, String testfile) {
		LemmaCandidateGenerator generator = trainer.train(LemmaInstance.getInstances(getResourceFile(trainfile)), null);
		testGenerator(generator, testfile);
	}

	private void testGenerator(LemmaCandidateGenerator generator, String testfile) {
//		Result result = Result.testGenerator(generator, getResourceFile(testfile));
//		result.logAccuracy();
//		result = Result.testGenerator(generator, getResourceFile(testfile + ".morfette"));
//		result.logAccuracy();
	}

	protected String getResourceFile(String name) {
		return INDEXES + String.format("res:///%s/%s", "marmot/test/lemma", name);
	}
	
	private final static String INDEXES = "form-index=4,lemma-index=5,tag-index=2,";
	
}
