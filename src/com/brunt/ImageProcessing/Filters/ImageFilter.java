package com.brunt.ImageProcessing.Filters;


import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/6/2014.
 */
public abstract class ImageFilter {
    public abstract BufferedImage FilterImage(BufferedImage inputImage);

    //Extract pixel intensity from average if RGB values
    public float ComputePixelIntensity(int pixel)
    {
        float pix = ((pixel>>16&0xff) + (pixel >>8&0xff) + (pixel&0xff))/3.0f;
        return pix;
    }

    //Create a new RGB pixel value from a float representing pixel intensity (GreyScale)
    public int CalcNewPix(float inPixl)
    {
        int newPix =(int)inPixl;
        return (newPix<<16 | newPix<<8 | newPix);
    }
}
