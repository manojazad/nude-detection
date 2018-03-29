package com.indrik.impl;

import com.indrik.detector.SkinDetector;
import com.indrik.model.SkinDetectedImage;
import com.indrik.util.classifier.HSVClassifier;
import com.indrik.util.classifier.NormalizedRGBClassifier;
import com.indrik.util.classifier.RGBClassifier;
import com.indrik.util.classifier.SkinColorClassifier;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BinaryImageSkinDetector implements SkinDetector {

  private static final int FOREGROUND_PIXEL = 1;
  private static final int BACKGROUND_PIXEL = 0;
  private List<SkinColorClassifier> skinClassifiers = new ArrayList<SkinColorClassifier>();

  public BinaryImageSkinDetector() {
    init();
  }

  private void init() {
    skinClassifiers.add(new RGBClassifier());
    skinClassifiers.add(new NormalizedRGBClassifier());
    skinClassifiers.add(new HSVClassifier());
  }

  public SkinDetectedImage detectSkin(ImagePlus image) {
    if (image == null) {
      throw new IllegalArgumentException("Input image cannot be null");
    }

    int skinPixelCount = countSkinPixels(image);

    SkinDetectedImage skinDetectedImage = new SkinDetectedImage();
    skinDetectedImage.setImage(image);
    skinDetectedImage.setSkinPixelCount(skinPixelCount);

    return skinDetectedImage;
  }

  private int countSkinPixels(ImagePlus image) {
    ImageProcessor imgProcessor = image.getProcessor();

    int skinPixelCount = 0;

    for (int y = 0; y < imgProcessor.getHeight(); y++) {
      for (int x = 0; x < imgProcessor.getWidth(); x++) {
        int pixel = imgProcessor.getPixel(x, y);

        boolean isSkinPixel = classifySkinPixel(pixel);
        if (isSkinPixel) {
          imgProcessor.putPixel(x, y, FOREGROUND_PIXEL);
          skinPixelCount++;
        } else {
          imgProcessor.putPixel(x, y, BACKGROUND_PIXEL);
        }
      }
    }
    return skinPixelCount;
  }

  private boolean classifySkinPixel(int pixel) {
    boolean isSkinPixel = false;
    Iterator<SkinColorClassifier> classifierIterator = skinClassifiers.iterator();
    while (classifierIterator.hasNext() && !isSkinPixel) {
      SkinColorClassifier skinClassifier = classifierIterator.next();
      isSkinPixel = skinClassifier.isSkinPixel(pixel);
    }
    return isSkinPixel;
  }
}
