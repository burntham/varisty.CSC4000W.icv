package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Theta;
import java.util.LinkedList;

/**
 * Created by Daniel on 8/11/2014.
 * Sobel Image Filter
 */
public class SobelFilter2 extends Filter {
    //Filter Values
    private float[] xFilterColumn = {1f,2f,1f}, xfilterRow={-1f,0f,1f}, yFilterColumn ={1f,0f,-1f}, yFilterRow ={1f,2f,1f};

    private LinkedList<Theta> gradientAngles;

    @Override
    public int[] filterImage(int[] original, int width, int height) {
        //Calulate gradients in X
        int[] gX = filter2DSeperableConvolution(original,width,height,xfilterRow,xFilterColumn);
        //Calculate gradients in Y
        int[] gY = filter2DSeperableConvolution(original,width,height,yFilterRow,yFilterColumn);
        //calculate gradient magnitudes with gMag(x,y) = Math.sqrt((gX(x,y)^2 + gY(x,y)^2))
        int[] gMag = computeMagnitudes(gX,gY, width,height);
        return gMag;
    }

    /**
     * Compute Gradient magnitudes -> populates a list of the non 0 ones.
     * @param gX
     * @param gY
     * @param width
     * @param height
     * @return
     */
    private int[] computeMagnitudes(int[] gX, int[] gY, int width, int height)
    {
        gradientAngles = new LinkedList<Theta>();
        int length = gX.length;
        int[] gMag = new int[gX.length];

        for (int i=0; i<length; i++)
        {
                int y=(i/width);
                int x=i-y*width;
                double mag = Math.sqrt(Math.pow(gX[i], 2) + Math.pow(gY[i], 2));
                gMag[i] = (int) mag;
            if(mag>0)
                gradientAngles.add(new Theta((x),(y),gX[i],gY[i]));
        }
        return gMag;
    }

    public LinkedList<Theta> getGradientAngles()
    {
        return gradientAngles;
    }

}
