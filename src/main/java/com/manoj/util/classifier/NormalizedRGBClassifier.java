package com.manoj.util.classifier;

import com.manoj.util.ColorUtil;

public class NormalizedRGBClassifier implements SkinColorClassifier {

	private static final int RED = 0;
	private static final int GREEN = 1;
	private static final int BLUE = 2;

	public boolean isSkinPixel(int pixel) {
		float[] normalizedRGBValues = ColorUtil.getNormalizedRGBValues(pixel);

		float normalRed = normalizedRGBValues[RED];
		float normalGreen = normalizedRGBValues[GREEN];
		float normalBlue = normalizedRGBValues[BLUE];
		
		return (normalRed / normalGreen > 1.185)
				&& ((normalRed * normalBlue)
						/ (Math.pow(normalRed + normalGreen + normalBlue, 2)) > 0.107)
				&& ((normalRed * normalGreen)
						/ (Math.pow(normalRed + normalGreen + normalBlue, 2)) > 0.112);
	}
}
