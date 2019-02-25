// Copyright 2013 Thomas Müller
// This file is part of MarMoT, which is licensed under GPLv3.

package marmot.util;

import java.io.Serializable;
import java.util.Arrays;

import marmot.core.Feature;



public class Encoder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int[] bytes_;
	private short current_array_length_;
	private short current_bit_index_;

	private State state_;
	
	public static class State implements Serializable {
		private static final long serialVersionUID = 1L;
		private short array_length_;
		private short bit_index_;
		private int byte_;	
	}
	
	public Encoder(int capacity) {
		bytes_ = new int[capacity];
		state_ = new State();
		reset();
	}

	public static int bitsNeeded(int max_value) {
		int num_bits = 1;
		while (true) {
			max_value /= 2;		
			if (max_value == 0) {
				break;
			}
			num_bits += 1;
		}		
		return num_bits;
	}

	public void append(boolean value) {
		append(value ? 0 : 1, 1);
	}

	public void append(int value, int bits_needed) {
		assert value >= 0;
		assert bitsNeeded(value) <= bits_needed : value;

		while (bits_needed != 0) {
			if (current_bit_index_ == Integer.SIZE) {
				current_array_length_ ++;
				current_bit_index_ = 0;
			}

			int bits_left = Integer.SIZE - current_bit_index_;
			int bits = Math.min(bits_left, bits_needed);
			int mask = (2 << (bits - 1)) - 1;			
			int b = value & mask;
			value >>= bits;
			bits_needed -= bits;
			
			bytes_[current_array_length_ - 1] += b << current_bit_index_;				
			current_bit_index_ += bits;
		}
		assert value == 0;
	}

	public Feature getFeature() {
		Feature feature = new Feature(bytes_.length);
		copyToFeature(feature);
		return feature;
	}

	public void reset() {
		current_array_length_ = 0;
		current_bit_index_ = Integer.SIZE;
		storeState();
		Arrays.fill(bytes_, 0);
	}

	public Feature getFeature(boolean flag) {
		return getFeature();
	}

	public void storeState() {
		storeState(state_);
	}
	
	public void storeState(State state) {
		state.array_length_ = current_array_length_;
		state.bit_index_ = current_bit_index_;
		if (current_array_length_ > 0)
			state.byte_ = bytes_[current_array_length_  - 1];
		else
			state.byte_ = 0;
	}

	public void restoreState() {
		restoreState(state_);
	}
	
	public void restoreState(State state) {
		Arrays.fill(bytes_, state.array_length_ , current_array_length_, 0);
		current_array_length_ = state.array_length_;
		current_bit_index_ = state.bit_index_;
		if (current_array_length_ > 0)
			bytes_[current_array_length_ - 1] = state.byte_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes_);
		result = prime * result + current_array_length_;
		result = prime * result + current_bit_index_;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Encoder other = (Encoder) obj;
		if (!Arrays.equals(bytes_, other.bytes_))
			return false;
		if (current_array_length_ != other.current_array_length_)
			return false;
		if (current_bit_index_ != other.current_bit_index_)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Encoder [bytes_=" + Arrays.toString(bytes_)
				+ ", current_array_length_=" + current_array_length_
				+ ", current_bit_index_=" + current_bit_index_
				+ ", stored_array_length_=" + state_.array_length_
				+ ", stored_bit_index_=" + state_.bit_index_
				+ ", stored_byte_=" + state_.byte_ + "]";
	}

	public int getCapacity() {
		return bytes_.length;
	}

	public void copyToFeature(Feature feature) {
		System.arraycopy(bytes_, 0, feature.getBytes(), 0, bytes_.length);
		feature.setArrayLength(current_array_length_);
		feature.setBitIndex(current_bit_index_);
	}

	public int getCurrentLength() {
		return current_array_length_;
	}

	
	
}
