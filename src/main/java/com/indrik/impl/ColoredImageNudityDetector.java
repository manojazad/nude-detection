package com.indrik.impl;

import com.indrik.detector.ImageNudityDetector;
import com.indrik.detector.SkinDetector;
import com.indrik.detector.SkinRegionDetector;
import com.indrik.model.SkinDetectedImage;
import com.indrik.model.SkinRegion;
import ij.ImagePlus;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ColoredImageNudityDetector implements ImageNudityDetector {
  private static final int PIXEL_COUNT_THRESHOLD = 10;
  private SkinDetector skinDetector;
  private SkinRegionDetector skinRegionDetector;

  public boolean isImageNude(Image inputImage) {
    ImagePlus image = new ImagePlus(inputImage.toString(), inputImage);

    SkinDetectedImage skinDetectedImage = detectSkinPixels(image);

    List<SkinRegion> skinRegions = detectSkinRegions(skinDetectedImage);
    filterOutSmallRegions(skinRegions);

    skinRegions.sort((skinRegion1, skinRegion2) -> {
      if (skinRegion1 == null || skinRegion2 == null) {
        throw new IllegalArgumentException("SkinRegion cannot be null");
      }
      int skinRegion1Pixels = skinRegion1.getNumberOfPixels();
      int skinRegion2Pixels = skinRegion2.getNumberOfPixels();

      if(skinRegion1Pixels > skinRegion2Pixels)
        return -1;
      else if(skinRegion1Pixels < skinRegion2Pixels)
        return 1;
      else
        return 0;
    });
    return evaluateNudity(image, skinDetectedImage, skinRegions);
  }

  private boolean evaluateNudity(ImagePlus image, SkinDetectedImage skinDetectedImage, List<SkinRegion> skinRegions) {
    int skinRegionsCount = skinRegions.size();
    int totalPixels = image.getWidth() * image.getHeight();
    int skinPixelCount = skinDetectedImage.getSkinPixelCount();
    if (skinRegionsCount < 3) {
      return false;
    }

    double skinPixelPercentage = (skinPixelCount * 100) / (totalPixels * 1.0);
    if (skinPixelPercentage < 15) {
      return false;
    }

    SkinRegion largestSkinRegion = skinRegions.get(0);
    SkinRegion secondLargestSkinRegion = skinRegions.get(1);
    SkinRegion thirdLargestSkinRegion = skinRegions.get(2);

    double skinPixelPercentLargest = (largestSkinRegion.getNumberOfPixels() / (skinPixelCount * 1.0)) * 100;
    double skinPixelPercent2ndLargest = (secondLargestSkinRegion.getNumberOfPixels() / (skinPixelCount * 1.0)) * 100;
    double skinPixelPercent3rdLargest = (thirdLargestSkinRegion.getNumberOfPixels() / (skinPixelCount * 1.0)) * 100;
    if (skinPixelPercentLargest < 35 && skinPixelPercent2ndLargest < 30
        && skinPixelPercent3rdLargest < 30) {
      return false;
    }

    if (skinPixelPercentLargest < 45) {
      return false;
    }

    return true;
  }

  private List<SkinRegion> detectSkinRegions(SkinDetectedImage skinDetectedImage) {
    skinRegionDetector = new BinaryImageSkinRegionDetector();
    List<SkinRegion> skinRegions = skinRegionDetector.detectSkinRegions(skinDetectedImage.getImage());
    return skinRegions;
  }

  private SkinDetectedImage detectSkinPixels(ImagePlus image) {
    skinDetector = new BinaryImageSkinDetector();
    SkinDetectedImage skinDetectedImage = skinDetector.detectSkin(image);
    return skinDetectedImage;
  }

  private void filterOutSmallRegions(List<SkinRegion> skinRegions) {
    SkinRegion[] regionsArray = new SkinRegion[skinRegions.size()];
    regionsArray = skinRegions.toArray(regionsArray);
    for (SkinRegion skinRegion : regionsArray) {
      if (skinRegion.getNumberOfPixels() < PIXEL_COUNT_THRESHOLD) {
        skinRegions.remove(skinRegion);
      }
    }
  }
}
