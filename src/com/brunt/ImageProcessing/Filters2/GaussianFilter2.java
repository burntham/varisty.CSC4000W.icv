package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Utils;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/11/2014.
 */
public class GaussianFilter2 extends Filter {

    //The actual Filter
    private float[] gFilter;

//    public GaussianFilter2(float sigma, int radius) {
//        this.gFilter = initFilter(sigma,radius);
//    }
//
//    public GaussianFilter2(BufferedImage original, float sigma, int radius) {
//        super(original);
//        this.gFilter = initFilter(sigma,radius);
//    }
//
//    public GaussianFilter2(int[] original, int width, int height, float sigma, int radius) {
//        super(original, width, height);
//        this.gFilter = initFilter(sigma,radius);
//    }

    private float[] initFilter( float sigma, int radius)
    {
        float[] newFilter = new float[radius*2-1];
        for (int i=0;i<2*radius-1;i++)
        {
            newFilter[i] = (float)Utils.getGaussian(i-radius,sigma);
        }
        return newFilter;
    }

    @Override
    public int[] filterImage(int[] original, int width, int height) {


        int[] filtered= filter1DConvolution(original,width,height,gFilter);
        Utils.transposeArr(width,height,filtered,null);
        int[] columnFiltered = filter1DConvolution(filtered,height,width,gFilter);
        Utils.transposeArr(height,width,filtered,null);
        System.out.println(filtered.length);
        return filtered;
    }




}
