package com.indrik.util.labeling;

import ij.process.ImageProcessor;

public abstract class FloodFillLabeling extends RegionLabeling {

	public FloodFillLabeling(ImageProcessor imgProcessor) {
		super(imgProcessor);
	}
	
	public void applyLabeling() {
		if (verbosity)
		resetLabel();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isForeground(x, y)) {
					int label = getNextLabel();
					if (verbosity)
					floodFill(x, y, label);
				}
			}
		}
	}
	
	protected abstract void floodFill(int x, int y, int label);

}
