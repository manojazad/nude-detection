package com.manoj.detector;

import com.manoj.model.SkinRegion;
import ij.ImagePlus;

import java.util.List;

public interface SkinRegionDetector {

  List<SkinRegion> detectSkinRegions(ImagePlus image);
}
