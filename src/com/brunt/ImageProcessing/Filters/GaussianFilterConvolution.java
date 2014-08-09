package com.brunt.ImageProcessing.Filters;

/**
 * Created by Daniel on 8/9/2014.
 */
public class GaussianFilterConvolution extends Filter2DSeperableConvolution {

    protected class GaussianFunc
    {
        private float sigma;

        GaussianFunc(float sigma)
        {
            this.sigma = sigma;
        }

        public double GaussIT(int x)
        {
            return ((1.0)/Math.sqrt(2*Math.PI*Math.pow(sigma,2)))*Math.exp(-(Math.pow(x,2)/(2*Math.pow(sigma,2))));
        }
    }

    private GaussianFunc G;
    private float[] filter;
    public GaussianFilterConvolution(float sigma, int radius) {
        super();
        initFilter(sigma,radius);
    }

    //Threshold Image after Gaussian blur
    public GaussianFilterConvolution(float sigma, int radius, int threshold) {
        super(threshold);
        initFilter(sigma,radius);
    }

    private void initFilter(float sigma, int radius)
    {
        G = new GaussianFunc(sigma);
        filter = new float[radius * 2 + 1];
        for (int i = 0; i <= radius * 2; i++) {
            filter[i] = (float) G.GaussIT(i - radius);
        }
        SetSymmetricSeperableFilter(filter);
    }

}
