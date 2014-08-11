package com.brunt.ImageProcessing.Filters2;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/11/2014.
 */
public abstract class Filter {
    protected int[] originalImage;
    protected int[] filteredImage;
    protected int originalWidth, originalHeight;

    Filter(BufferedImage original)
    {
        originalImage = createIntArrayFromImg(original);
    }

    public Filter(int[] original, int width, int height)
    {
        this.originalImage = original;
        this.originalHeight = height;
        this.originalWidth = width;
    }

    /**
     * Set call filterImage to create a new filtererd int[] dataset,
     * @return
     */
public abstract int[] filterImage();

    /**
     * Return a GreyScal Version of the array
     * @param inputArr
     * @param width
     * @param height
     * @return
     */
    public BufferedImage getGreyScaleBufferedImage(int[] inputArr, int width, int height)
    {
        BufferedImage newImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for (int y=0; y<height; y++)
        {
            for ( int x = 0; x< width;x ++)
            {
                newImage.setRGB(x,y,getGreyValue(inputArr[y*height+x]));
            }
        }
        return newImage;
    }

    /**
     * Return the filtered image, will return previously data if it exists or it will call methods to create filtered data
     * @return
     */
    public int[] getFilteredData()
    {
        if(filteredImage==null)
            filteredImage = filterImage();

        return filteredImage;
    }

    /**
     * Calculate the GreyScaleValue
     * @param pix
     * @return
     */
    private int getGreyValue(int pix)
    {
        return (int)(Math.min(255, ((pix>>16&0xff)+(pix>>8&0xff)+(pix&0xff))/3.0f));
    }

    /**
     * Convert Buffered image into an int array
     * @param original
     * @return
     */
    private int[] createIntArrayFromImg(BufferedImage original)
    {
        originalHeight = original.getHeight();
        originalHeight = original.getWidth();
        int[] newArr = new int[originalHeight*originalWidth];
        for (int y=0; y<originalHeight;y++)
        {
            for (int x=0; x<originalWidth;x++)
            {
                newArr[y*originalWidth+x] = original.getRGB(x,y) &0xffffff;
            }
        }
        return newArr;
    }

    protected int[] filter1DConvolution(int[] filter)
    {
        int[] filtered = new int[originalHeight*originalWidth];


        filteredImage=filtered;
        return filtered;
    }



}
