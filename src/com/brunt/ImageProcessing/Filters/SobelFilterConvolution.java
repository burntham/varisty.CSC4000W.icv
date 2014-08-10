package com.brunt.ImageProcessing.Filters;

import com.brunt.ImageProcessing.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Daniel on 8/9/2014.
 *
 */
public class SobelFilterConvolution extends Filter2DSeperableConvolution {
    private float[] xFilterColumn = {1f,2f,1f}, xfilterRow={-1f,0f,1f}, yFilterColumn ={1f,0f,-1f}, yFilterRow ={1f,2f,1f};
    private int[][] theta;
    private LinkedList<Point> edges;
    private Filter2DSeperableConvolution gX, gY;

    private LinkedList<Theta> thetas;

    public SobelFilterConvolution(){
        super();
    }

    public int[][] FilterImage(BufferedImage inputImage) {
        //Compute different Axis gradients

        int[][] intedImage = Utils.ConvertBuffImageToIntArr(inputImage);
        int[][] intGradX = ConvolveRows(ConvolveColumns(intedImage,xFilterColumn),xfilterRow);
        int[][] intGradY = ConvolveRows(ConvolveColumns(intedImage,yFilterColumn), yFilterRow);

        int[][] intMagnitude = GenerateGradientMagnitudes(intGradX,intGradY);
        int[][] supressed = NonMaximSuppression(intMagnitude);
        //BufferedImage testMag = Utils.convertIntArrToBufferedImage(supressed);
        return supressed;
    }

    protected int[][] GenerateGradientMagnitudes(int[][] gX, int[][] gY)
    {
        int width=gX[0].length, height = gX.length;
        int[][] out = new int[height][width];
        theta =  new int[height][width];
        thetas = new LinkedList<Theta>();

        //The out buffered image is used to store gradient magnitudes, and as such the normal colour model is ignored - Improvements incoming
        for ( int y=0; y< height; y++)
        {
            for ( int x=0; x<width;x++)
            {
                int gradX = gX[y][x], gradY = gY[y][x];
                int newVal = (int) Math.sqrt(Math.pow(gradX, 2) + Math.pow(gradY, 2));
                if(newVal>1)
                    thetas.add(new Theta(x,y,groupAtan(gradX,gradY)));

                //Store Gradient magnitude values in the buffer.
                out[y][x]= newVal;
            }
        }
        return out;
    }

    private int groupAtan(int gradX, int gradY)
    {
        double theta=0.0f;

        theta = Math.abs(theta);
        theta = (theta>180)? theta-180:theta;

        int block=0;

        //Group Theta into a 'block' either 0,45,90 or 135 - to be used for Non-Maximum Supression
        if ( theta <= 22.5 || theta >= 157.5)
            block =0;//0deg
        else if ( theta <=67.5)
            block = 45;//45
        else if(theta<=112.5)
            block =90;//90
        else
            block = 135;//135

        return block;
    }

    private int[][] NonMaximSuppression(int[][] input)
    {
        int width = input[0].length, height = input.length;
        int[][] newSuppressed = new int[height][width];
        edges = new LinkedList<Point>();

        ListIterator<Theta> thetaIterator = thetas.listIterator();
        while(thetaIterator.hasNext())
        {
            Theta thetaPoint = thetaIterator.next();
            int x=(int)thetaPoint.getX(),y= (int)thetaPoint.getY();
            int max = input[y][x]; int newMax=0;
            int theta = thetaPoint.getTheta();
            for (int i=-1; i<2;i+=2)
            {
                try
                {
                    if (theta == 0 )
                        newMax = Math.max( input[y][x+i],newMax);
                    else if (theta == 45)
                        newMax =Math.max(newMax, input[y+i][x+i]);
                    else if (theta == 90)
                        newMax =Math.max(newMax, input[y+i][x]);
                    else if (theta == 135)
                        newMax =Math.max(newMax, input[y-i][x+i]);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    //Do nothing
                }
            }
            if (max >= newMax)
            {
                newSuppressed[y][x] = 255;
                edges.add(new Point(x,y));
            }
            else
                newSuppressed[y][x]=0;


        }
        return newSuppressed;
    }

    public LinkedList<Point> getEdges()
    {
        return edges;
    }

    protected int[][] ConvolveRows(int[][] input, float[] filter)
    {
        int[][] output = new int[input.length][input[0].length];
        int offset = filter.length/2;
        int columns = input[0].length;
        int rows = input.length;

        for ( int y = 0; y<rows; y++)
        {
            for (int x = 0; x <columns; x++)
            {

                float newValue = 0.0f;

                for ( int j =0; j<filter.length;j++)
                {
                    int index=0;
                    if ( x- offset+j <0)
                        index=0;
                    else if(x-offset+j>=columns)
                        index = columns-1;
                    else
                        index = x-offset+j;

                    newValue += input[y][index]*filter[j];
                }
                output[y][x] = (int)newValue;
            }
        }
        return output;
    }

    protected int[][] ConvolveColumns(int[][] input, float[] filter)
    {
        int[][] output = new int[input.length][input[0].length];
        int offset = filter.length/2;
        int columns = input[0].length;
        int rows = input.length;

        for ( int x = 0; x<columns; x++)
        {
            for (int y = 0; y <rows; y++)
            {
                float newValue = 0.0f;

                for ( int j =0; j<filter.length;j++)
                {
                    int index=0;
                    if ( y- offset+j <0)
                        index=0;
                    else if(y-offset+j>=rows)
                        index = rows-1;
                    else
                        index = y-offset+j;

                    newValue += input[index][x]*filter[j];
                }
                output[y][x] = (int)newValue;
            }
        }
        return output;
    }
}
