package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Filters.Theta;

import java.util.LinkedList;

/**
 * Created by Daniel on 8/11/2014.
 */
public class SobelFilter2 extends Filter {
    private float[] xFilterColumn = {1f,2f,1f}, xfilterRow={-1f,0f,1f}, yFilterColumn ={1f,0f,-1f}, yFilterRow ={1f,2f,1f};
    private LinkedList<Theta> gradientAngles;

    public SobelFilter2() {

    }

    @Override
    public int[] filterImage(int[] original, int width, int height) {
        int[] gX = filter2DSeperableConvolution(original,width,height,xfilterRow,xFilterColumn);
        int[] gY = filter2DSeperableConvolution(original,width,height,yFilterRow,yFilterColumn);
        int[] gMag = computeMagnitudes(gX,gY, width,height);
        return gMag;
    }

    private int[] computeMagnitudes(int[] gX, int[] gY, int width, int height)
    {
        gradientAngles = new LinkedList<Theta>();
        int length = gX.length;
        int[] gMag = new int[gX.length];

        for (int i=0; i<length; i++)
        {
            if((gX[i]>0) && (gY[i]>0) )
            {
                gMag[i] = (int) Math.sqrt(Math.pow(gX[i], 2) + Math.pow(gY[i], 2));
                gradientAngles.add(new Theta((i%width),(i%height),gX[i],gY[i]));
            }

        }

        return gMag;
    }

    public LinkedList<Theta> getGradientAngles()
    {
        return gradientAngles;
    }

}
