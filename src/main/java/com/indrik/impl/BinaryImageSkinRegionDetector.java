package com.indrik.impl;

import com.indrik.detector.SkinRegionDetector;
import com.indrik.model.SkinRegion;
import com.indrik.util.labeling.BreadthFirstLabeling;
import com.indrik.util.labeling.RegionLabeling;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.List;

public class BinaryImageSkinRegionDetector implements SkinRegionDetector {
  private RegionLabeling labeling;

  public List<SkinRegion> detectSkinRegions(ImagePlus image) {
    ImageProcessor imgProcessor = image.getProcessor();
    labeling =  new BreadthFirstLabeling(imgProcessor);
    return labeling.getRegions();
  }
}
