package com.jason.app.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    private static final int IMG_WIDTH = 280;
    private static final int IMG_HEIGHT = 220;
    private final String IMAGE_PATH= "resource/background.jpg";

    public void printNewImage (String imagePath) {
        try {

            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImageJpg = resizeImage(originalImage, type);
            ImageIO.write(resizeImageJpg, "jpg", new File(IMAGE_PATH));

        } catch (IOException e) {
            System.out.println("打不开"+imagePath);
        }
    }

    public JLabel setBackGrondImageLabel () {
        ImageIcon background = new ImageIcon(IMAGE_PATH);
        JLabel label = new JLabel(background);
        label.setBounds(0,0,background.getIconWidth(),background.getIconHeight());
        return label;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }
}
