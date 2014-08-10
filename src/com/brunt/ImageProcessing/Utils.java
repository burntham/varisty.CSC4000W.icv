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
                newBuffImage.setRGB(x,y,original[y][x]);
            }
        }

        System.out.println("Int Array converted to Buffered Image");

        return newBuffImage;
    }

}
