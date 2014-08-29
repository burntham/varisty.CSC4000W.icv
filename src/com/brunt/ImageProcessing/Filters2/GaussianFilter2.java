package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Utils;

/**
 * Created by Daniel on 8/11/2014.
 * Gaussian Filter
 */
public class GaussianFilter2 extends Filter {

    //The actual Filter
    private float[] gFilter;

    public GaussianFilter2(float sigma, int radius) {
        this.gFilter = initFilter(sigma,radius);
        float sum=0;
        for (int i=0; i<gFilter.length;i++)
        {
            sum+=gFilter[i];
        }
    }

    /**
     * Generate 1D Gauss Filter (can be used for 2D filtering too!)
     * @param sigma
     * @param radius Filter radius
     * @return
     */
    private float[] initFilter( float sigma, int radius)
    {
        float[] newFilter = new float[radius*2+1];
        for (int i=0;i<=2*radius;i++)
        {
            newFilter[i] = (float)Utils.getGaussian(i-radius,sigma);
        }
        return newFilter;
    }

    @Override
    public int[] filterImage(int[] original, int width, int height) {
        int [] filtered = filter2DSeperableConvolution(original,width,height, gFilter, gFilter);
        return filtered;
    }




}
