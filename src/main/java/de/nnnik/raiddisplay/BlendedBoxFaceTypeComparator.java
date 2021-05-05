package de.nnnik.raiddisplay;

import java.util.Comparator;


public class BlendedBoxFaceTypeComparator implements Comparator<BlendedBoxRenderer> {
	
	private static final BlendedBoxFaceTypeComparator instance = new BlendedBoxFaceTypeComparator();
	
	private BlendedBoxFaceTypeComparator() {}
	
	public static BlendedBoxFaceTypeComparator getInstance() {
		return instance;
	}
	
	@Override
	public int compare(BlendedBoxRenderer arg0, BlendedBoxRenderer arg1) {
		return Double.compare(arg1.getDistanceSq(), arg0.getDistanceSq());
	}
	
}