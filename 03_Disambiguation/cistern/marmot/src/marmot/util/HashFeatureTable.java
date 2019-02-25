package marmot.util;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class HashFeatureTable implements FeatureTable {

	private static final long serialVersionUID = 1L;
	private TIntSet set_; 
	
	public HashFeatureTable() {
		set_ = new TIntHashSet();
	}
	
	@Override
	public int size() {
		return set_.size();
	}

	@Override
	public int getFeatureIndex(Encoder encoder, boolean insert) {
		int hash_code = encoder.hashCode();
		if (hash_code < 0) {
			hash_code = -hash_code;
		}
		
		if (set_.contains(hash_code)) {
			return hash_code;
		}
		
		if (insert) {
			set_.add(hash_code);
			return hash_code;
		}
		
		return -1;
	}
	
//	private void writeObject(ObjectOutputStream oos) throws IOException {
//		oos.defaultWriteObject();
//		oos.writeInt(set_.size());
//		for (Integer i : set_) {
//			oos.writeInt(i);
//		}
//	}
//
//	private void readObject(ObjectInputStream ois)
//			throws ClassNotFoundException, IOException {
//		ois.defaultReadObject();
//		int size = ois.readInt();
//		set_ = new HashSet<>(size);
//		for (int number = 0; number < size; number++) {
//			Integer i = ois.readInt();
//			set_.add(i);
//		}
//	}
}
