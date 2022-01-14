/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.skillfactory.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;

/**
 *
 * @author hoshi
 */
public class ImageUtils {

  private static File createTempFile(String type) throws IOException {
    if (type.contains("png"))
      return Files.createTempFile(null, ".png").toFile();
    return Files.createTempFile(null, ".jpg").toFile();
  }

  public static File createSquaredImage(InputStream imgSrc, String type) throws IOException {
    type = type.toLowerCase();
    BufferedImage origImg = ImageIO.read(imgSrc);
    File tempFile = null;
    BufferedImage squaredImage = createSquaredImage(origImg);
    String squaredImgType = "png";
    if (squaredImage == origImg && !type.contains("png"))
      squaredImgType = "jpg";
    tempFile = createTempFile(squaredImgType);
    try {
      ImageIO.write(squaredImage, squaredImgType, tempFile);
    } catch (IOException ex) {
      try {
        Files.deleteIfExists(tempFile.toPath());
      } catch (Exception ex2) {
      }
      throw ex;
    }
    return tempFile;
  }

  public static BufferedImage createSquaredImage(BufferedImage origImg) throws IOException {
    int origHeight = origImg.getHeight();
    int origWidth = origImg.getWidth();
    int newHeight = origHeight;
    int newWidth = origWidth;
    double ratio;
    if (origHeight > origWidth) {
      newWidth = origHeight;
      ratio = origWidth / (double) newWidth;
    } else {
      newHeight = origWidth;
      ratio = origHeight / (double) newHeight;
    }
    if (ratio >= 0.95)
      return origImg;
    return createSquaredImage(origImg, origWidth, origHeight, newWidth, newHeight);

  }

  private static BufferedImage createSquaredImage(BufferedImage origImg, int origWidth, int origHeight, int newWidth,
      int newHeight) {
    BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = bufferedImage.createGraphics();

    // To be sure, we use clearRect, which will (unlike fillRect) totally replace
    // the current pixels with the desired color, even if it's fully transparent.
    graphics.setBackground(new Color(0, true));
    graphics.clearRect(0, 0, newWidth, newHeight);

    if (newWidth > origWidth)
      graphics.drawImage(origImg, (newWidth - origWidth) / 2, 0, null);
    else
      graphics.drawImage(origImg, 0, (newHeight - origHeight) / 2, null);
    graphics.dispose();
    return bufferedImage;
  }

}
