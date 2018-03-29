package com.indrik.detector;

import com.indrik.model.SkinDetectedImage;
import ij.ImagePlus;

public interface SkinDetector {

  SkinDetectedImage detectSkin(ImagePlus inputImage);
}
