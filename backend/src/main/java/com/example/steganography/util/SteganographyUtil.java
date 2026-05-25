package com.example.steganography.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class SteganographyUtil {

    public static byte[] encode(byte[] imageBytes, String message) throws Exception {

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (image == null) {
            throw new Exception("Invalid image format. Use PNG image.");
        }

        String finalMessage = message + "###END###";

        int messageIndex = 0;
        int charValue = 0;
        int bitIndex = 0;

        outerLoop:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int pixel = image.getRGB(x, y);

                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                if (messageIndex < finalMessage.length()) {

                    charValue = finalMessage.charAt(messageIndex);

                    int bit = (charValue >> (7 - bitIndex)) & 1;

                    blue = (blue & 0xFE) | bit;

                    bitIndex++;

                    if (bitIndex == 8) {
                        bitIndex = 0;
                        messageIndex++;
                    }

                } else {
                    image.setRGB(x, y,
                            (alpha << 24) |
                            (red << 16) |
                            (green << 8) |
                            blue
                    );
                    break outerLoop;
                }

                int newPixel =
                        (alpha << 24) |
                        (red << 16) |
                        (green << 8) |
                        blue;

                image.setRGB(x, y, newPixel);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(image, "png", baos);

        return baos.toByteArray();
    }

    public static String decode(byte[] imageBytes) throws Exception {

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (image == null) {
            throw new Exception("Invalid image format.");
        }

        StringBuilder message = new StringBuilder();

        int currentByte = 0;
        int bitCount = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int pixel = image.getRGB(x, y);

                int blue = pixel & 0xff;

                int bit = blue & 1;

                currentByte = (currentByte << 1) | bit;

                bitCount++;

                if (bitCount == 8) {

                    char c = (char) currentByte;

                    message.append(c);

                    if (message.toString().endsWith("###END###")) {
                        return message.substring(0,
                                message.length() - 9);
                    }

                    bitCount = 0;
                    currentByte = 0;
                }
            }
        }

        return "No hidden message found.";
    }
}