package com.brunt.ImageProcessing.Filters2;

import com.brunt.ImageProcessing.Filters.Theta;
import com.sun.javafx.geom.Edge;

import javax.swing.plaf.TableHeaderUI;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Daniel on 8/11/2014.
 */
public class CannyEdgeDetection {
    private GaussianFilter2 gaussFilter;
    private SobelFilter2 sobelFilter;
    private int tLow, tHigh;//thresholds for Hysteresis thresholding

    CannyEdgeDetection(int tLow,int tHigh)
    {
        gaussFilter = new GaussianFilter2(1.4f,2);
        sobelFilter = new SobelFilter2();
        this.tLow = tLow;
        this.tHigh = tHigh;
    }

    public LinkedList<Edge> detectEdges(int[] original, int width, int height)
    {
        LinkedList<Edge> edges = new LinkedList<Edge>();

        int[] gaussedImage=gaussFilter.filterImage(original,width,height);
        int[] sobeledGaussed = sobelFilter.filterImage(gaussedImage,width,height);
        LinkedList<Theta> gradientThetas = sobelFilter.getGradientAngles();
        int[] suppressedSobel = nonMaximSupression(original, width, height, gradientThetas);


        return edges;
    }

    private int[] nonMaximSupression(int[] original, int width, int height, LinkedList<Theta> gradients)
    {
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

            if(max>newMax)
                newSupressed[y*height+x] = 255;

        }


        return newSupressed;
    }

    private int safeCheck(int[] original, int width, int height, int x, int y)
    {
        int obtained =0;
        if ((x>=0)&&(x<width))
            if((y>=0)&&(y<height))
                return original[y*height+x];

        return obtained;

    }

}
