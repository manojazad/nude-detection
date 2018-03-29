package com.manoj.impl;

import com.manoj.detector.SkinRegionDetector;
import com.manoj.model.SkinRegion;
import com.manoj.util.labeling.BreadthFirstLabeling;
import com.manoj.util.labeling.RegionLabeling;
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
