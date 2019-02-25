// Copyright 2013 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package marmot.morph.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class FileOptions {
	public static final String FORM_INDEX = "form-index";
	public static final String LEMMA_INDEX = "lemma-index";
	public static final String TAG_INDEX = "tag-index";
	public static final String MORPH_INDEX = "morph-index";
	public static final String LIMIT = "limit";
	public static final String FST_MORPH_INDEX = "token-feature-index";

	private int form_index_;
	private int lemma_index_;
	private int tag_index_;
	private int morph_index_;
	private List<Integer> token_feature_index_;
	private String filename_;
	private int limit_;

	public FileOptions(String option_string) {
		parse(option_string);
	}

	private void parse(String option_string) {
		form_index_ = -1;
		lemma_index_ = -1;
		tag_index_ = -1;
		morph_index_ = -1;
		limit_ = -1;
		token_feature_index_ = new LinkedList<Integer>();
		filename_ = null;

		String[] args = option_string.split(",");
		for (String arg : args) {

			if (arg.length() == 0) {
				continue;
			}

			int index = arg.indexOf('=');

			if (index < 0) {
				if (filename_ != null) {
					throw new RuntimeException(
							"Option string contains more than one filename: "
									+ option_string);
				}
				filename_ = arg;
			} else {
				String option = arg.substring(0, index);
				String value = arg.substring(index + 1, arg.length());

				if (option.equalsIgnoreCase(FORM_INDEX)) {
					if (form_index_ != -1) {
						throw new RuntimeException(
								"Option string contains more than one form index: "
										+ option_string);
					}
					form_index_ = Integer.parseInt(value);
				}  else if (option.equalsIgnoreCase(LEMMA_INDEX)) {
					if (lemma_index_ != -1) {
						throw new RuntimeException(
								"Option string contains more than one lemma index: "
										+ option_string);
					}
					lemma_index_ = Integer.parseInt(value);
				} else if (option.equalsIgnoreCase(TAG_INDEX)) {
					if (tag_index_ != -1) {
						throw new RuntimeException(
								"Option string contains more than one tag index: "
										+ option_string);
					}
					tag_index_ = Integer.parseInt(value);
				} else if (option.equalsIgnoreCase(MORPH_INDEX)) {
					if (morph_index_ != -1) {
						throw new RuntimeException(
								"Option string contains more than one morph index: "
										+ option_string);
					}
					morph_index_ = Integer.parseInt(value);

				} else if (option.equalsIgnoreCase(FST_MORPH_INDEX)) {
					token_feature_index_.add(Integer.parseInt(value));
				} else if (option.equalsIgnoreCase(LIMIT)) {
					if (limit_ != -1) {
						throw new RuntimeException(
								"Option string contains more than one limit: "
										+ option_string);
					}
					limit_ = Integer.parseInt(value);
				} else {
					throw new RuntimeException("Unknown option: " + option);
				}
			}

		}

		if (filename_ == null) {
			throw new RuntimeException("No filename in option string: "
					+ option_string);
		}
	}

	public String getFilename() {
		return filename_;
	}

	public int getLimit() {
		return limit_;
	}

	public int getFormIndex() {
		return form_index_;
	}

	public int getTagIndex() {
		return tag_index_;
	}

	public int getMorphIndex() {
		return morph_index_;
	}

	public void setTagIndex(int index) {
		tag_index_ = index;
	}

	public void setMorphIndex(int index) {
		morph_index_ = index;
	}

	public InputStream getInputStream() {

		InputStream input_stream = null;

		try {

			if (filename_.toLowerCase().startsWith("res://")) {
				
				String name = filename_.substring(6);
				input_stream = getClass().getResourceAsStream(name);
				
				if (input_stream == null) {
					throw new RuntimeException("Resource not found: " + filename_);
				}
				
			} else {
			
				input_stream = new FileInputStream(filename_);
				
			}
			
			if (filename_.toLowerCase().endsWith(".gz")) {
				input_stream = new GZIPInputStream(input_stream);
			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return input_stream;
	}

//	public void setInputStream(InputStream input_stream) {
//		input_stream_ = input_stream;
//	}

	public List<Integer> getTokenFeatureIndex() {
		return token_feature_index_;
	}

	public void dieIfPropertyIsEmpty(String property) {
		if (morph_index_ == -1) {
			System.err.format("Error: File property '%s' needs to be set!\n",
					property);
			System.exit(1);
		}
	}

	public int getLemmaIndex() {
		return lemma_index_;
	}

}
 