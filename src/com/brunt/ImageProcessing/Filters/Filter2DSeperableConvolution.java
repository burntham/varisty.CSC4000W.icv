package com.brunt.ImageProcessing.Filters;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/8/2014.
 */
public class Filter2DSeperableConvolution extends ImageFilter {

    private float[] filterRow,filterColumn;
    private boolean enableThreshold = false;
    private int threshold;

    Filter2DSeperableConvolution()
    {

    }

    //Construct a simple object that just works with the default methods
    public Filter2DSeperableConvolution(float[] filterRow, float[] filterColumn)
    {
        this.filterRow = filterRow;
        this.filterColumn = filterColumn;
    }

    public Filter2DSeperableConvolution(int threshold)
    {
        this.enableThreshold = true;
        this.threshold = threshold;
    }

    protected void SetSymmetricSeperableFilter(float[] filter)
    {
        this.filterRow = filter; this.filterColumn = filter;

    }

    @Override
    public BufferedImage FilterImage(BufferedImage inputImage) {

        return ConvolveRows(ConvolveColumns(inputImage, filterColumn, false), filterRow,false);
    }

    protected BufferedImage ConvolveRows(BufferedImage input, float[] filter, boolean edges)
    {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(),input.getType());
        int offset = filter.length/2;
        int columns = input.getWidth();
        int rows = input.getHeight();

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
                    newValue += ComputePixelIntensity(input.getRGB(index, y))*filter[j];
                }
                //If if thresholding is disabled or new value exceeds threshold, update the output image.
                if(!enableThreshold || (enableThreshold && newValue > threshold))
                    if(!edges)
                        output.setRGB(x,y,CalcNewPix(newValue));
                    else
                        output.setRGB(x,y,(int)newValue);
            }
        }
        return output;
    }

    protected BufferedImage ConvolveColumns(BufferedImage input, float[] filter, boolean edges)
    {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        int offset = filter.length/2;
        int columns = input.getWidth();
        int rows = input.getHeight();

        for ( int x = 0; x<columns; x++)
        {
            for (int y = 0; y <rows; y++)
            {
                float newValue = 0.0f;

                for ( int j =0; j<filter.length;j++)
                {
                    int index=0;
                    if ( y- offset+j <0)
                        index=0;
                    else if(y-offset+j>=rows)
                        index = rows-1;
                    else
                        index = y-offset+j;
                    newValue += ComputePixelIntensity(input.getRGB(x, index))*filter[j];
                }
                if(!edges)
                    output.setRGB(x,y,CalcNewPix(newValue));
                else
                    output.setRGB(x,y,(int)newValue);
            }
        }
        return output;
    }



}
