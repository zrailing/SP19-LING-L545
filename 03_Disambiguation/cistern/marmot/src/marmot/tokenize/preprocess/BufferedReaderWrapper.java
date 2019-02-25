// Copyright 2014 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package marmot.tokenize.preprocess;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedReaderWrapper implements InternalReader {


	private BufferedReader reader_;

	public BufferedReaderWrapper(BufferedReader reader) {
		reader_ = reader;
	}

	@Override
	public void mark() {
		try {
			reader_.mark(10000);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reset() {
		try {
			reader_.reset();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String readLine() {
		try {
			return reader_.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
