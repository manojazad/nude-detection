package com.manoj.util.classifier;

import com.manoj.util.ColorUtil;

public class HSVClassifier implements SkinColorClassifier {
	
	private static final int HUE = 0;
	private static final int SATURATION = 1;

	public boolean isSkinPixel(int pixel) {
		float[] hsvValues = ColorUtil.getHSVValues(pixel);
		return (hsvValues[HUE] >= 0 
				&& hsvValues[HUE] <= 50 
				&& hsvValues[SATURATION] >= 0.23 
				&& hsvValues[SATURATION] <= 0.68);
	}
}
