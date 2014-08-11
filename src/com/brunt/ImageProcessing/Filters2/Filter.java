package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Utils;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/11/2014.
 */
public abstract class Filter {

    /**
     * Set call filterImage to create a new filtererd int[] dataset,
     * @return
     */
    public abstract int[] filterImage(int[] original, int width, int height);

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
                    newValue += (int)input[y*width+index]*filter[j];
                }
                filtered[y*width+x] = (int)newValue;
            }
        }
        return filtered;
    }
}
