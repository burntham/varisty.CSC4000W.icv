package com.brunt.ImageProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Daniel on 8/5/2014.
 */
public class ImageManager {
    public static BufferedImage ReadImage(String fileName)
    {

        File file = new File(fileName);
        Deb((file.canRead() ? fileName + " Read successfully" : fileName + " Failed"));

        BufferedImage original = null;
        BufferedImage smoothed = null;

        try{
            original = ImageIO.read(file);
            //originalImage = new BufferedImage(i.getWidth(),i.getHeight(),BufferedImage.TYPE_INT_RGB);


        }
        catch (Exception e){
            Deb("Error Loading image");
        }

        return original;
    }

    //Lazy debug printing
    public static void Deb(String message)
    {
        System.out.println(message);
    }
}
