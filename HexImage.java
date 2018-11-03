import javax.imageio.*;
import java.io.*;
import java.awt.Image.*;
import java.awt.Window.Type;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class HexImage {

    public static final String inputFile = "saved.png";
    public static final String hexFile = "hex.png";
    public static final String outputFile = "hexMap.png";
    public static final Color borderColor = new Color(0,0,0);
    public static final int xOffset = 62;

    public static void main(String[] args) {
        try{
            
            System.out.println("Running");
            //Read in input file into ARGB format
            BufferedImage input = ImageIO.read(new File(inputFile));
            BufferedImage hexBase = ImageIO.read(new File(hexFile));

            //Create output image
            int width = input.getWidth() * xOffset + hexBase.getWidth() - xOffset;
            int height = (int)(input.getHeight() * hexBase.getHeight() + hexBase.getHeight() * .5);
            
            BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int totalVolume = input.getWidth() * input.getHeight();
            int calculated = 0;
            System.out.println("Building...");
            for(int y = 0; y < input.getHeight(); y++) {
                for(int x = 0; x < input.getWidth(); x++) {
                    System.out.println(((float)calculated / (float)totalVolume) * 100 + "%");
                    calculated++;
                    int rgb = input.getRGB(x,y);

                    int hexTopLeftY = y * hexBase.getHeight() + (x % 2 == 0 ? 0 : hexBase.getHeight() / 2);
                    int hexTopLeftX = x * xOffset;

                    for(int subY = 0; subY < hexBase.getHeight(); subY++) {
                        for(int subX = 0; subX < hexBase.getWidth(); subX++) {
                            int hexRGB = hexBase.getRGB(subX, subY);
                            Color c = new Color(hexRGB);
                            int xPixel = hexTopLeftX + subX;
                            int yPixel = hexTopLeftY + subY;
                            if(c.getBlue() == 0) {
                                //Hex border
                                out.setRGB(xPixel, yPixel, borderColor.getRGB());
                            } else if(c.getRed() == 0) {
                                //Internals
                                out.setRGB(xPixel, yPixel, rgb);
                            }
                        }
                    }

                }
            }

            System.out.println("Writing...");
            ImageIO.write(out, "png", new File(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}