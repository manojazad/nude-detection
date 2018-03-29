package com.manoj.util.labeling;

import com.manoj.model.SkinRegion;
import ij.ImagePlus;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class RegionLabeling {

	protected static boolean doDisplay = false;

	protected static final int BACKGROUND = 0;
	protected static final int FOREGROUND = 1;
	protected static final int START_LABEL = 2;
	protected int[] labels = null;
	protected int width;
	protected int height;
	protected boolean verbosity = true;
	private int currentLabel;
	private int maxLabel;

	List<SkinRegion> regions = null;

	protected RegionLabeling(ImageProcessor ip) {
		width = ip.getWidth();
		height = ip.getHeight();

		makeLabelArray(ip);
		applyLabeling();
		collectRegions();
	}

	private void makeLabelArray(ImageProcessor ip) {
		labels = new int[width * height];
		for (int v = 0; v < height; v++) {
			for (int u = 0; u < width; u++) {
				int p = ip.getPixel(u, v);
				if (p > 0) {
					labels[v * width + u] = FOREGROUND;
				} else {
					labels[v * width + u] = BACKGROUND;
				}
			}
		}
	}

	protected abstract void applyLabeling();

	private void collectRegions() {
		SkinRegion[] regionArray = new SkinRegion[maxLabel + 1];
		for (int i = START_LABEL; i <= maxLabel; i++) {
			regionArray[i] = new SkinRegion(i);
		}
		// scan the labels array and collect the coordinates for each region
		for (int v = 0; v < height; v++) {
			for (int u = 0; u < width; u++) {
				int lb = labels[v * width + u];
				if (lb >= START_LABEL && lb <= maxLabel
						&& regionArray[lb] != null) {
					regionArray[lb].addPixel(u, v);
				}
			}
		}

		// collect all nonempty regions and create the final list of regions
		List<SkinRegion> regionList = new ArrayList<>();
		for (SkinRegion r : regionArray) {
      if (r != null && r.getNumberOfPixels() > 0) {
				r.updateRegionStatistics();
				regionList.add(r);
			}
		}
		regions = regionList;
	}

	boolean isForeground(int u, int v) {
		return (labels[v * width + u] == FOREGROUND);
	}

	void resetLabel() {
		currentLabel = -1;
		maxLabel = -1;
	}

	int getNextLabel() {
		if (currentLabel < 1) {
			currentLabel = START_LABEL;
		} else {
			currentLabel = currentLabel + 1;
		}
		maxLabel = currentLabel;
		return currentLabel;
	}

	void setLabel(int u, int v, int label) {
		if (u >= 0 && u < width && v >= 0 && v < height) {
			labels[v * width + u] = label;
		}
	}

	public static void setDisplay(boolean display) {
		doDisplay = display;
	}

	public void setVerbosity(boolean verbosity) {
		this.verbosity = verbosity;
	}

	public List<SkinRegion> getRegions() {
		return regions;
	}

	public int getMaxLabel() {
		return maxLabel;
	}

	public int getLabel(int u, int v) {
		if (u >= 0 && u < width && v >= 0 && v < height) {
			return labels[v * width + u];
		} else {
			return BACKGROUND;
		}
	}

	public boolean isLabel(int u, int v) {
		return (labels[v * width + u] >= START_LABEL);
	}

	public ImageProcessor makeLabelImage(boolean color) {
		if (color) {
			return makeRandomColorImage();
		} else {
			return makeGrayImage();
		}
	}

	public FloatProcessor makeGrayImage() {
		FloatProcessor ip = new FloatProcessor(width, height, labels);
		ip.resetMinAndMax();
		return ip;
	}

	public ColorProcessor makeRandomColorImage() {
		int[] colorLUT = new int[maxLabel + 1];

		for (int i = START_LABEL; i <= maxLabel; i++) {
			colorLUT[i] = makeRandomColor();
		}

		ColorProcessor cp = new ColorProcessor(width, height);
		int[] colPix = (int[]) cp.getPixels();

		for (int i = 0; i < labels.length; i++) {
			if (labels[i] >= 0 && labels[i] < colorLUT.length) {
				colPix[i] = colorLUT[labels[i]];
			} else {
				throw new Error("illegal label = " + labels[i]);
			}
		}
		return cp;
	}

	protected void showLabelArray() {
		ImageProcessor ip = new FloatProcessor(width, height, labels);
		ip.resetMinAndMax();
		ImagePlus im = new ImagePlus("Label Array", ip);
		im.show();
	}

	protected int makeRandomColor() {
		double saturation = 0.2;
		double brightness = 0.2;
		float h = (float) Math.random();
		float s = (float) (saturation * Math.random() + 1 - saturation);
		float b = (float) (brightness * Math.random() + 1 - brightness);
		return Color.HSBtoRGB(h, s, b);
	}

	void snooze(int time) {
		if (time > 0) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
