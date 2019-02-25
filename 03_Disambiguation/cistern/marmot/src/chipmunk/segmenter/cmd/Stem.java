package chipmunk.segmenter.cmd;

import java.io.IOException;
import java.util.Iterator;

import marmot.util.FileUtils;
import chipmunk.segmenter.SegmentationDataReader;
import chipmunk.segmenter.Segmenter;
import chipmunk.segmenter.SegmenterOptions;
import chipmunk.segmenter.Stemmer;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

public class Stem {

	private static final String INPUT_FILE = "input-file";
	private static final String MODEL_FILE = "model-file";
	private static final String OUTPUT_FILE = "output-file";
	private static final String MODE = "mode";
	
	public static void main(String[] args) throws JSAPException, IOException {
		FlaggedOption opt;
		JSAP jsap = new JSAP();

		opt = new FlaggedOption(INPUT_FILE).setRequired(true).setLongFlag(INPUT_FILE);
		jsap.registerParameter(opt);
		
		opt = new FlaggedOption(OUTPUT_FILE).setRequired(true).setLongFlag(OUTPUT_FILE);
		jsap.registerParameter(opt);

		opt = new FlaggedOption(MODEL_FILE).setRequired(true).setLongFlag(MODEL_FILE);
		jsap.registerParameter(opt);
		
		opt = new FlaggedOption(MODE ).setDefault("ROOT_DETECTION").setLongFlag(MODE);
		jsap.registerParameter(opt);

		SegmenterOptions options = new SegmenterOptions();
		options.registerOptions(jsap);

		JSAPResult config = jsap.parse(args);

		if (!config.success()) {
			for (Iterator<?> errs = config.getErrorMessageIterator(); errs
					.hasNext();) {
				System.err.println("Error: " + errs.next());
			}
			System.err.println("Usage: ");
			System.err.println(jsap.getUsage());
			System.err.println(jsap.getHelp());
			System.err.println();
			System.exit(1);
		}

		Stemmer.Mode mode = Stemmer.Mode.valueOf(config.getString(MODE).toUpperCase().replace("-", "_"));
		
		options.setOptions(config);
		
		Segmenter segmenter = FileUtils.loadFromFile(config.getString(MODEL_FILE));
		SegmentationDataReader reader = new SegmentationDataReader(config.getString(INPUT_FILE), options.getString(SegmenterOptions.LANG), options.getInt(SegmenterOptions.TAG_LEVEL));
		
		Stemmer stemmer = new Stemmer(segmenter, mode);
		
		stemmer.stemToFile(config.getString(OUTPUT_FILE), reader);		
	}
	
}
