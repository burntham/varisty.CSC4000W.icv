package com.brunt.ImageProcessing;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/10/2014.
 * Utils Class-> used for odds and ends
 */
public class Utils {

    /**
     * Function used to debug binary operations
     * @param number the int value for which binary representation is needed
     */
    public static void printBinary(int number)
    {
        StringBuffer binBuffer = new StringBuffer();

        for (int i=31;i>=0;i--)
        {
            binBuffer.append((number>>i)&1);
        }
        System.out.println(number+" is:\n"+binBuffer.toString());
    }

//FILTERS2 Utils///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Return a GreyScal Version of the array
     * @param inputArr Image int array
     * @param width image width
     * @param height image height
     * @return
     */
    public static BufferedImage getGreyScaleBufferedImage(int[] inputArr, int width, int height)
    {
        BufferedImage newImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for (int y=0; y<height; y++)
        {
            for ( int x = 0; x< width;x ++)
            {
                newImage.setRGB(x,y,setGreyIntensity(inputArr[y*width+x]));
            }
        }
        return newImage;
    }

    public static BufferedImage getRawColouredBufferedImage(int[] inputArr, int width, int height)
    {
        BufferedImage newImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for (int y=0; y<height; y++)
        {
            for ( int x = 0; x< width;x ++)
            {
                newImage.setRGB(x,y,Math.abs(inputArr[y*width+x]));
            }
        }
        return newImage;
    }

    public static BufferedImage getIntensityBufferedImage(int[] inputArr, int width, int height)
    {
        BufferedImage newImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);


        for (int y=0; y<height; y++)
        {
            for ( int x = 0; x< width;x ++)
            {
                newImage.setRGB(x,y,getPixelMaxIntensity(inputArr[y*width+x]));
            }
        }
        return newImage;
    }

    /**
     * Calculate the GreyScaleValue
     * @param pix
     * @return
     */
    public static int getPixelAvgIntensity(int pix)
    {
        return (int)(((pix>>16&0xff)+(pix>>8&0xff)+(pix&0xff))/3.0f);
    }

    /**
     * Get value of highest intensity colour
     * @param pix
     * @return
     */
    public static int getPixelMaxIntensity(int pix)
    {
        return Math.max(pix&0xff, Math.max(pix>>8&0xff, pix>>16&0xff));
    }

    /**
     * Return an int representing the greyscale intensity
     * @param pix desired intesnity value 0-255;
     * @return
     */
    public static int setGreyIntensity(int pix)
    {
        return (int)((((pix&0xff)<<16) | ((pix&0xff)<<8) | (pix&0xff)));
    }

    /**
     * Gaussian Function g(x,sigma)
     * @param x
     * @param sigma
     * @return
     */
    public static double getGaussian(int x, float sigma)
    {
        return ((1.0)/Math.sqrt(2*Math.PI*Math.pow(sigma,2)))*Math.exp(-(Math.pow(x,2)/(2*Math.pow(sigma,2))));
    }


    /**
     * Convert Buffered image into an int array
     * @param original
     * @return
     */
    public static int[] createIntArrayFromImg(BufferedImage original)
    {
        int height = original.getHeight(), width = original.getWidth();
        int[] newArr = new int[height*width];
        for (int y=0; y<height;y++)
        {
            for (int x=0; x<width;x++)
            {
                newArr[y*width+x] = Utils.getPixelAvgIntensity(original.getRGB(x,y));
            }
        }
        return newArr;
    }

}
