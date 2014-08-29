package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Theta;
import java.awt.*;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Daniel on 8/11/2014.
 * Almost complete Canny edge detection : Still requires Hysteresis thresholding and basic thresholding after Gaussian blur
 */
public class CannyEdgeDetection {
    private GaussianFilter2 gaussFilter;
    private SobelFilter2 sobelFilter;
    private int tLow, tHigh;//thresholds for Hysteresis thresholding
    private LinkedList<Point> edges;

    public CannyEdgeDetection(int tLow,int tHigh)
    {
        gaussFilter = new GaussianFilter2(1.4f,2);
        sobelFilter = new SobelFilter2();
        this.tLow = tLow;
        this.tHigh = tHigh;
    }

    /**
     * Process image and get an array of detected Edges
     * @param original
     * @param width
     * @param height
     * @return
     */
    public int[] detectEdges(int[] original, int width, int height)
    {
        int[] gaussedImage=gaussFilter.filterImage(original,width,height);
        int[] sobeledGaussed = sobelFilter.filterImage(gaussedImage,width,height);
        LinkedList<Theta> gradientThetas = sobelFilter.getGradientAngles();
        int[] suppressedSobel = nonMaximSupression(sobeledGaussed, width, height, gradientThetas);
        return suppressedSobel;
    }

    /**
     * Reduce Edges through non-maximum supression
     * @param original
     * @param width
     * @param height
     * @param gradients
     * @return
     */
    private int[] nonMaximSupression(int[] original, int width, int height, LinkedList<Theta> gradients)
    {
        edges = new LinkedList<Point>();
        int[] newSupressed = new int[width*height];

        ListIterator<Theta> thetaIter = gradients.listIterator();

        while (thetaIter.hasNext())
        {
            Theta edgeTheta = thetaIter.next();
            int x = edgeTheta.x;
            int y = edgeTheta.y;
            int theta = edgeTheta.getTheta();
            int max = original[y*width+x];
            int newMax = 0;

            for (int i=-1; i<2; i+=2)
            {
                if (theta == 0)
                    newMax = Math.max(newMax, safeCheck(original,width,height,x+i,y));
                else if(theta==45)
                    newMax = Math.max(newMax, safeCheck(original,width,height,x+i,y+i));
                else if (theta==90)
                    newMax = Math.max(newMax, safeCheck(original,width,height,x+i,y));
                else if (theta==135)
                    newMax = Math.max(newMax, safeCheck(original,width,height,x-i,y+i));
            }

            if(max>=newMax) {
                newSupressed[y * width + x] = max;
                edges.add(new Point(x,y));
            }
        }
        return newSupressed;
    }

    /**
     * Get a LinkedList of Edges found by the Canny Detection
     * @return
     */
    public LinkedList<Point> getEdges()
    {
        return edges;
    }

    /**
     * Prevent Index out of bound errors
     * @param original
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private int safeCheck(int[] original, int width, int height, int x, int y)
    {
        if ((x>=0)&&(x<width))
            if((y>=0)&&(y<height))
                return original[y*width+x];

        return 0;
    }

}
