package com.brunt.ImageProcessing;

import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilterConvolution;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Daniel on 8/10/2014.
 */
public class HoughTransform {
    private int[][][] accumulator;
    private int radMin, radMax, differentRadii;
    private GaussianFilterConvolution gaussianFilter;
    private SobelFilterConvolution sobelEdgeDetector;
    private BufferedImage inputImage;
    private int imageWidth, imageHeight;

    public HoughTransform(BufferedImage inputImage, int[] radiusRange, float gaussianSigma,int gaussianRadius, int threshold){
        this.inputImage=inputImage;
        radMin = radiusRange[0];
        radMax=radiusRange[1];
        differentRadii = radMax-radMin;

        //Load Guassian FIlters etc independantly, until Canny detection implemented properly...
        if(threshold>0)
            gaussianFilter = new GaussianFilterConvolution(gaussianSigma, gaussianRadius,threshold);
        else
            gaussianFilter = new GaussianFilterConvolution(gaussianSigma, gaussianRadius);
        sobelEdgeDetector = new SobelFilterConvolution();
        imageHeight = inputImage.getHeight();
        imageWidth = inputImage.getWidth();
    }

    public LinkedList<Discs> detectDiscs()
    {
        accumulator = new int[differentRadii][imageHeight][imageWidth];
        populateAcummulator();
        LinkedList<Discs> discList = exctractDiscsFromAcc();

        return discList;
    }

    /**
     * Populate the accumulator
     */
    private void populateAcummulator()
    {
        int[][] edgeDetected = sobelEdgeDetector.FilterImage(Utils.convertIntArrToBufferedImage(gaussianFilter.FilterImage(inputImage)));

        for ( int y=0; y<imageHeight;y++)
        {
            for ( int x=0; x<imageWidth; x++)
            {
                if(edgeDetected[y][x]!=0)
                {
                    for (int rad = radMin; rad<radMax; rad++)
                    {
                        bresCircle(x,y,rad);
                    }
                }
            }
        }
    }

    /**
     * Generate a LinkedList of discs, by detecting local maxima in accumulator
     * @return
     */
    private LinkedList<Discs> exctractDiscsFromAcc()
    {
        LinkedList<Discs> discList = new LinkedList<Discs>();
        for (int radius =radMin; radius < radMax; radius++)
        {
            int approxIntecepts = (int)(approximateIntercepts(radius)*0.7f);
            for ( int y=0; y<imageHeight;y++)
            {
                for ( int x=0; x<imageWidth;x++)
                {
                    if(accumulator[radius-radMin][y][x]>approxIntecepts)
                    {
                        discList.add(new Discs(x,y,radius));
                    }
                }
            }
        }
        return discList;
    }


    /**Follow Similar process to bresenheims circle drawing to approximate the number of pixels on a circumference->
     * should be close to the number of interceptions for a given radius on a detected circle
     * @param radius The radius of the circle
     * @return
     */
    private int approximateIntercepts(int radius)
    {
            int x=0, y=radius , xCenter=0, yCenter=0, count=0;
            int d = 3 -2*radius;
            while(x<y) {
                count += 8;
                x = x + 1;
                if (d < 0)
                    d += 4 * x + 6;
                else {
                    y--;
                    d += 4 * (x - y) + 10;
                }
                count += 8;
            }
            return count;
    }

    /**
     * Bresenheims Circle drawing algorithm
     * @param xCenter
     * @param yCenter
     * @param radius
     */
    private void bresCircle(int xCenter, int yCenter, int radius)
    {
        int x=0, y=radius;
        int d = 3 -2*radius;

        while(x<y)
        {
            drawCircle(xCenter,yCenter,x,y,radius);
            x=x+1;
            if(d<0)
                d+=4*x+6;
            else {
                y--;
                d+=4*(x-y)+10;
            }
            drawCircle(xCenter,yCenter,x,y,radius);
        }
    }

    private void drawCircle(int xCenter,int yCenter, int x, int y, int radius)
    {
        drawPixel(xCenter+x,yCenter+y,radius);//1
        drawPixel(xCenter-x,yCenter+y,radius);//2
        drawPixel(xCenter+x,yCenter-y,radius);//3
        drawPixel(xCenter-x,yCenter-y,radius);//4
        drawPixel(xCenter+y,yCenter+x,radius);//5
        drawPixel(xCenter-y,yCenter+x,radius);//6
        drawPixel(xCenter+y,yCenter-x,radius);//7
        drawPixel(xCenter-y,yCenter-x,radius);//8

    }

    private void drawPixel(int x,int y, int radius)
    {
        if (x>=0 && x<imageWidth)
            if(y>=0 && y<imageHeight)
                accumulator[radius-radMin][y][x] +=1;
    }

    public BufferedImage drawAccumulator()
    {
        if(accumulator==null)
            return null;

        int[][] temp = new int[imageHeight][imageWidth];

        for (int y=0;y<imageHeight;y++)
        {
            for (int x=0; x<imageWidth;x++)
            {
                for ( int ran= 0; ran<differentRadii; ran++)
                {
                    float normalizer = (1.0f/approximateIntercepts(ran+radMin));
                    temp[y][x] += (accumulator[ran][y][x]*normalizer)*255;
                }
            }
        }
        return Utils.convertIntArrToBufferedImage(temp);
    }
}
