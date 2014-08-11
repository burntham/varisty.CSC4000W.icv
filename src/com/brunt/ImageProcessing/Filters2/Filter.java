package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Utils;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/11/2014.
 */
public abstract class Filter {
    protected int[] originalImage;
    protected int[] filteredImage;
    protected int originalWidth, originalHeight;

    public Filter()
    {

    }

    public Filter(BufferedImage original)
    {
        originalImage = Utils.createIntArrayFromImg(original);
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
    public abstract int[] filterImage(int[] original);

//    /**
//     * Return the filtered image, will return previously data if it exists or it will call methods to create filtered data
//     * @return
//     */
//    public int[] getFilteredData()
//    {
//        if(filteredImage==null)
//            filteredImage = filterImage();
//
//        return filteredImage;
//    }




    protected int[] filter1DConvolution(int[] input,int width,int height, float[] filter)
    {
        int[] filtered = new int[width*height];
        int offset = filter.length/2;
        int columns = width;
        int rows = height;

        for ( int y = 0; y<rows; y++)
        {
            for (int x = 0; x <columns; x++)
            {
                float newValue = 0.0f;

                for ( int j =0; j<filter.length;j++)
                {
                    int index=0;
                    if ( x- offset+j <0)
                        index=0;
                    else if(x-offset+j>=columns)
                        index = columns-1;
                    else
                        index = x-offset+j;
                    newValue += input[y*width+index]*filter[j];
                }
                filtered[y*height+x] = (int)newValue;
            }
        }
        return filtered;
    }



}
