package com.brunt.ImageProcessing;

import com.brunt.ImageProcessing.Filters2.CannyEdgeDetection;
import com.brunt.ImageProcessing.Filters2.GaussianFilter2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Daniel on 8/10/2014.
 * Hough Transform Disc detection
 */
public class HoughTransform2 {
    private int[][][] accumulator;
    private int minRadius, maxRadius;
    private GaussianFilter2 gaussianFilter;
    private CannyEdgeDetection cannyDetector;
    private int imageWidth, imageHeight;

    public HoughTransform2( int minRadius, int maxRadius, float gaussianSigma,int gaussianRadius){
        this.minRadius = minRadius;
        this.maxRadius=maxRadius;

        gaussianFilter = new GaussianFilter2(gaussianSigma,gaussianRadius);
        cannyDetector = new CannyEdgeDetection(5,10);

    }

    /**
     * Process the image and return a Linked list containing the detected discs
     * @param original The image int arr
     * @param width image width
     * @param height image height
     * @param minRadius The minimum disc radius to search for
     * @param maxRadius The maximum disc radius to search for
     * @param sigma Gauss function Sigma
     * @param gaussRadius Gauss filter Radius
     * @return
     */
    public LinkedList<Discs> detectDiscs(int[] original,int width,int height, int minRadius, int maxRadius, float sigma,int gaussRadius)
    {
        this.minRadius = minRadius;
        this.maxRadius=maxRadius;
        this.imageHeight = height;
        this.imageWidth = width;

        int radiusRange = maxRadius-minRadius;
        accumulator = new int[radiusRange][imageHeight][imageWidth];
        int[] blurred = gaussianFilter.filterImage(original,width,height);
        int[] edgeDetected = cannyDetector.detectEdges(blurred,width,height);
        LinkedList<Point> edges = cannyDetector.getEdges();
        populateAccumulator(edges);
        LinkedList<Discs> discList = exctractDiscsFromAcc();

        return discList;
    }

    /**
     * Populates the accumulator with circles used to detect discs in the oriignal image (generates the hough space)
     * @param edges linked list containing the detected edges
     */
    private void populateAccumulator(LinkedList<Point> edges)
    {
        ListIterator<Point> edgeIter=  edges.listIterator();

        while(edgeIter.hasNext())
        {
            Point edge = edgeIter.next();

            for (int rad = minRadius; rad<maxRadius; rad++)
            {
                bresCircle(edge.x,edge.y,rad);
            }
        }
    }

    /**
     * Generate a LinkedList of discs, by detecting local maxima in accumulator
     * @return Linked list of discs
     */
    private LinkedList<Discs> exctractDiscsFromAcc()
    {
        LinkedList<Discs> discList = new LinkedList<Discs>();
        for (int radius = minRadius; radius < maxRadius; radius++)
        {
            int approxIntecepts = (int)(approximateIntercepts(radius)*0.5f);
            for ( int y=0; y<imageHeight;y++)
            {
                for ( int x=0; x<imageWidth;x++)
                {
                    if(accumulator[radius- minRadius][y][x]>=approxIntecepts)
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
                accumulator[radius- minRadius][y][x] +=1;
    }

    /**
     * Return a Buffered Image for a view of the Hough space
     * @return
     */
    public BufferedImage drawAccumulator()
    {
        int radiusRange = maxRadius-minRadius;
        int minIntercepts = approximateIntercepts(minRadius);
        int maxIntercepts = approximateIntercepts(maxRadius);
        int interceptRange = maxIntercepts-minIntercepts;
        if(accumulator==null)
            return null;

        int[] temp = new int[imageHeight*imageWidth];

        for ( int ran= 0; ran<radiusRange; ran++)
        {
            for (int y=0;y<imageHeight;y++)
            {
                for (int x=0; x<imageWidth;x++)
                {
                    float intercepts = Math.min(approximateIntercepts(accumulator[ran][y][x]),accumulator[ran][y][x]);
                    float normaliZed = Math.abs((intercepts-minIntercepts)/(interceptRange*1.0f));
                    temp[y*imageWidth+x] += (intercepts*normaliZed);
                }
            }
        }
        return Utils.getGreyScaleBufferedImage(temp,imageWidth,imageHeight);
    }
}
