package com.indrik.detector;

import com.indrik.model.SkinRegion;
import ij.ImagePlus;

import java.util.List;

public interface SkinRegionDetector {

  List<SkinRegion> detectSkinRegions(ImagePlus image);
}
