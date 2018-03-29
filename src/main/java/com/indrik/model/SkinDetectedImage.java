package com.indrik.model;

import ij.ImagePlus;

public class SkinDetectedImage {

  private ImagePlus image;
  private int skinPixelCount;

  public ImagePlus getImage() {
    return image;
  }

  public void setImage(ImagePlus image) {
    this.image = image;
  }

  public int getSkinPixelCount() {
    return skinPixelCount;
  }

  public void setSkinPixelCount(int skinPixelCount) {
    this.skinPixelCount = skinPixelCount;
  }
}
