package com.brunt.ImageProcessing;

import com.sun.javafx.binding.StringFormatter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel on 8/5/2014.
 */
public class ImageManager {
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

    //Write Buffered Image to a GIF
    public static boolean WriteImage(String fileName, BufferedImage img)
    {
        Boolean response = false;

        try {
            File file = new File(String.format("%s.gif",fileName));
            Deb(file.canRead() ? fileName + " Exists already, overwriting" : "writing to " + fileName);
            ImageIO.write(img, "GIF", file);
        }
        catch (IOException e)
        {
            Deb("Error writing to file");
        }

        return response;
    }
    //Lazy debug printing
    public static void Deb(String message)
    {
        System.out.println(message);
    }
}
