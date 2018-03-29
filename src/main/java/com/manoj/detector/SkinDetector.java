package com.manoj.detector;

import com.manoj.model.SkinDetectedImage;
import ij.ImagePlus;

public interface SkinDetector {

  SkinDetectedImage detectSkin(ImagePlus inputImage);
}
