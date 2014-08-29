package com.brunt.ImageProcessing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel on 8/5/2014.
 * Image I/O
 */
public class ImageManager {

    /**
     * Read the .gif file into a buffered image
     * @param fileName
     * @return
     */
    public static BufferedImage ReadImage(String fileName)
    {
        File file = new File(fileName);
        Deb((file.canRead() ? fileName + " Read successfully" : fileName + " Failed"));
        BufferedImage original = null;

        try{
            original = ImageIO.read(file);
        }
        catch (Exception e){
            Deb("Error Loading image");
        }
        return original;
    }

    /**
     * Write the final Buffered Image to a .gif
     * @param fileName
     * @param img
     */
    public static void WriteImage(String fileName, BufferedImage img)
    {
        try {
            File file = new File(String.format("%s.gif",fileName));
            Deb(file.canRead() ? fileName + " Exists already, overwriting" : "writing to " + fileName+".gif");
            ImageIO.write(img, "GIF", file);
        }
        catch (IOException e)
        {
            Deb("Error writing to file");
        }
    }
    //Lazy debug printing
    public static void Deb(String message)
    {
        System.err.println(message);
    }
}
