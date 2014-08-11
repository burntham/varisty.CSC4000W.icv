package com.brunt.ImageProcessing;

import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilterConvolution;

import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/10/2014.
 */
public class HoughTransform {
    private int[][][] accumulator;
    private int radMin, radMax;
    private GaussianFilterConvolution gaussianFilter;
    private SobelFilterConvolution sobelEdgeDetector;
    private BufferedImage inputImage;
    private int imageWidth, imageHeight;

    public HoughTransform(BufferedImage inputImage, int[] radiusRange, float gaussianSigma,int gaussianRadius, int threshold){
        this.inputImage=inputImage;
        radMin = radiusRange[0];
        radMax=radiusRange[1];

        //Load Guassian FIlters etc independantly, until Canny detection implemented properly...
        if(threshold>0)
            gaussianFilter = new GaussianFilterConvolution(gaussianSigma, gaussianRadius,threshold);
        else
            gaussianFilter = new GaussianFilterConvolution(gaussianSigma, gaussianRadius);
        sobelEdgeDetector = new SobelFilterConvolution();
        imageHeight = inputImage.getHeight();
        imageWidth = inputImage.getWidth();
        accumulator = new int[radMax][imageHeight][imageWidth];

    }

    public void detectDiscs()
    {
        int[][] edgeDetected = sobelEdgeDetector.FilterImage(Utils.convertIntArrToBufferedImage(gaussianFilter.FilterImage(inputImage)));

        for ( int y=0; y<imageHeight;y++)
        {
            for ( int x=0; x<imageWidth; x++)
            {
                if(edgeDetected[y][x]!=0)
                {
                    //System.out.print(x);
                    for (int rad = radMin; rad<radMax; rad++)
                    {
                        bresCircle(x,y,rad);
                    }
                }
            }
            //System.out.println();;
        }
    }

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
        drawPixel(xCenter+x,yCenter+y,radius);
        drawPixel(xCenter-x,yCenter+y,radius);
        drawPixel(xCenter+x,yCenter-y,radius);
        drawPixel(xCenter-x,yCenter-y,radius);
        drawPixel(xCenter+y,yCenter+x,radius);
        drawPixel(xCenter-y,yCenter+x,radius);
        drawPixel(xCenter+y,yCenter-x,radius);
        drawPixel(xCenter-y,yCenter-y,radius);

    }

    private void drawPixel(int x,int y, int radius)
    {
        if (x>=0 && x<imageWidth)
            if(y>=0 && y<imageHeight)
                accumulator[radius][y][x] +=1;
    }

    public BufferedImage drawAccumulator()
    {
        int[][] temp = new int[imageHeight][imageWidth];

        for (int y=0;y<imageHeight;y++)
        {
            for (int x=0; x<imageWidth;x++)
            {
                for ( int ran= radMin; ran<radMax; ran++)
                {
                    temp[y][x] += accumulator[ran][y][x];
                }
            }
        }

        return Utils.convertIntArrToBufferedImage(temp);
    }
}
