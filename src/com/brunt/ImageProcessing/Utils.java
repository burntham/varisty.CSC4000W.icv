package com.brunt.ImageProcessing;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/10/2014.
 */
public class Utils {

    /**
     * Function used to debug binary operations
     * @param number
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

    /**
     * @param original The Bufferedimage to be converted, removing alpha
     * @return the int array
     */
    public static int[][] ConvertBuffImageToIntArr(BufferedImage original)
    {
        int height = original.getHeight(),width = original.getWidth();
        int[][] intArray = new int[height][width];

        for ( int y=0;y<height;y++)
        {
            for ( int x=0; x<width;x++)
            {
                intArray[y][x] = original.getRGB(x,y)&0xffffff;
            }
        }

        System.out.println("Buffered Image converted to Int Array");
        return intArray;
    }

    public static BufferedImage convertIntArrToBufferedImage(int[][] original)
    {
        int height=original.length, width = original[0].length;
        BufferedImage newBuffImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for ( int y=0; y<height;y++)
        {
            for (int x=0; x<width;x++)
            {
                int newPix = original[y][x];
                newPix = (newPix>255)?255:newPix;
                newBuffImage.setRGB(x,y,newPix<<16 | newPix<<8 | newPix);
            }
        }

        System.out.println("Int Array converted to Buffered Image");

        return newBuffImage;
    }
//FILTERS2 Utils///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Return a GreyScal Version of the array
     * @param inputArr
     * @param width
     * @param height
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

    /**
     * Calculate the GreyScaleValue
     * @param pix
     * @return
     */
    public static int getPixelAvgIntensity(int pix)
    {
        return (int)(((pix>>16&0xff)+(pix>>8&0xff)+(pix&0xff))/3.0f);
    }

    public static int setGreyIntensity(int pix)
    {
        return (int)((((pix&0xff)<<16) | ((pix&0xff)<<8) | (pix&0xff)));
    }

    /**
     * Gaussian Function
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
