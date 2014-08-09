package com.brunt.ImageProcessing.Filters;

/**
 * Created by Daniel on 8/9/2014.
 */
public class GaussianFilter extends Filter2DSeperable {

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
    public GaussianFilter(float sigma, int radius) {
        super();
        G = new GaussianFunc(sigma);
        float test= 0.0f;

        filter = new float[radius*2+1];


        for(int i=0; i<=radius*2;i++)
        {
            filter[i] = (float)G.GaussIT(i-radius);
        }

        for ( int i=0; i< filter.length; i++)
        {
            test+= filter[i];
        }

        System.out.println("Summed Values of Gaussian filter "+test);
        System.out.print(filter.length);
        SetSymmetricSeperableFilter(filter);
    }
}
