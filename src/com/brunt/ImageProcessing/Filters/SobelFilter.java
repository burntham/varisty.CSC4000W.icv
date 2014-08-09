package com.brunt.ImageProcessing.Filters;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/9/2014.
 *
 */
public class SobelFilter extends Filter2DSeperableConvolution {
    private float[] xFilterColumn = {1f,2f,1f}, xfilterRow={-1f,0f,1f}, yfilterColumn={1f,0f,-1f}, yfilterRow={1f,2f,1f};
    private int[][] theta;
    private Filter2DSeperableConvolution gX, gY;

    public SobelFilter(){
        super();
    }

    @Override
    public BufferedImage FilterImage(BufferedImage inputImage) {
        BufferedImage gradientX  = ConvolveRows(ConvolveColumns(inputImage, xFilterColumn,true), xfilterRow,true);
        BufferedImage gradientY = ConvolveRows(ConvolveColumns(inputImage,yfilterColumn,true),yfilterRow,true);


        return GenerateGradientMagnitudes(gradientX, gradientY);
    }

    protected BufferedImage GenerateGradientMagnitudes(BufferedImage gX, BufferedImage gY)
    {
        //The out buffered image is used to store gradient magnitudes, and as such the normal colour model is ignored - Improvements incoming
        BufferedImage out = gX;
        theta = new int[gX.getHeight()][gX.getWidth()];

        for ( int y=0; y< gX.getHeight(); y++)
        {
            for ( int x=0; x<gX.getWidth();x++)
            {
                //Store Gradient values in the buffer
                int gradX = gX.getRGB(x,y), gradY = gY.getRGB(x,y);
                int newVal =  (int)Math.sqrt(Math.pow(gradX,2)+Math.pow(gradY,2));
                out.setRGB(x,y,(newVal));
            }
        }
        return out;
    }

    private void groupAtan(int gradX, int gradY)
    {

    }
}
