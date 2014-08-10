package com.brunt.ImageProcessing.Filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/9/2014.
 *
 */
public class SobelFilterConvolution extends Filter2DSeperableConvolution {
    private float[] xFilterColumn = {1f,2f,1f}, xfilterRow={-1f,0f,1f}, yFilterColumn ={1f,0f,-1f}, yFilterRow ={1f,2f,1f};
    private int[][] theta;
    private Filter2DSeperableConvolution gX, gY;

    public SobelFilterConvolution(){
        super();
    }

    @Override
    public BufferedImage FilterImage(BufferedImage inputImage) {
        //Compute different Axis gradients
//        BufferedImage gradientX  = ConvolveRows(ConvolveColumns(inputImage, xFilterColumn,true), xfilterRow,true);
//        BufferedImage gradientY = ConvolveRows(ConvolveColumns(inputImage, yFilterColumn,true), yFilterRow,true);

        int[][] intedImage = BImagetoIntArr(inputImage);
        int[][] intGradX = ConvolveRows(ConvolveColumns(intedImage,xFilterColumn),xfilterRow);
        int[][] intGradY = ConvolveRows(ConvolveColumns(intedImage,yFilterColumn), yFilterRow);

//        int one = gradientX.getRGB(0,0), two = gradientY.getRGB(0,0);
//
//        System.out.println(new Color(one)+" "+one +" "+new Color(two) + " "+two);
//        BufferedImage magnitude = GenerateGradientMagnitudes(gradientX, gradientY);
//        BufferedImage supressed = NonMaximSuppression(magnitude);

        int[][] intMagnitude = GenerateGradientMagnitudes(intGradX,intGradY);
        BufferedImage testMag = IntArrToBImage(intMagnitude);
        return testMag;
    }

    protected int[][] GenerateGradientMagnitudes(int[][] gX, int[][] gY)
    {
        int width=gX[0].length, height = gX.length;
        int[][] out = new int[height][width];
        theta =  new int[height][width];

        //The out buffered image is used to store gradient magnitudes, and as such the normal colour model is ignored - Improvements incoming

        for ( int y=0; y< height; y++)
        {
            for ( int x=0; x<width;x++)
            {
                int gradX = gX[y][x], gradY = gY[y][x];
                    theta[y][x] = groupAtan(gradX, gradY);
                    int newVal = (int) Math.sqrt(Math.pow(gradX, 2) + Math.pow(gradY, 2));

                    //Store Gradient magnitude values in the buffer.
                    out[y][x]= newVal;
            }
        }
        return out;
    }

    private int groupAtan(int gradX, int gradY)
    {
        double theta;
        try{
            theta = Math.atan2(gradX,gradY);
        }catch (Exception e)
        {
           theta = 1;//0deg
        }

        theta = Math.abs(theta);
        theta = (theta>180)? theta-180:theta;

        int block=0;

        //Group Theta into a 'block' either 0,45,90 or 135 - to be used for Non-Maximum Supression
        if ( theta <= 22.5 || theta >= 157.5)
            block =1;//0deg
        else if ( theta <=67.5)
            block = 2;//45
        else if(theta<=112.5)
            block =4;//90
        else
            block = 8;//135

        return block;
    }

    private BufferedImage NonMaximSuppression(BufferedImage gradients)
    {
        BufferedImage newSuppressed = new BufferedImage(gradients.getWidth(),gradients.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y=0;y<gradients.getHeight();y++)
        {
            for ( int x=0;x< gradients.getWidth();x++)
            {
                        int max = gradients.getRGB(x, y) & 0xffffff; int newMax=0;
                        for (int i=-1; i<2;i+=2)
                        {
                           try
                           {
                               if(theta[y][x]!=0) {
                                   if (theta[y][x] == 1 )
                                       newMax = Math.max(gradients.getRGB(x + i, y) & 0xffffff,newMax);
                                   else if (theta[y][x] == 2)
                                       newMax =Math.max(newMax,gradients.getRGB(x + i, y + i) & 0xffffff);
                                   else if (theta[y][x] == 4)
                                       newMax =Math.max(newMax,gradients.getRGB(x, y + i) & 0xffffff);
                                   else if (theta[y][x] == 8)
                                       newMax =Math.max(newMax,gradients.getRGB(x + i, y - i) & 0xffffff);
                               }
                           }catch (Exception e){

                           }
                        }
                    if (max >= newMax)
                        newSuppressed.setRGB(x,y,max);
                }
        }
        return newSuppressed;
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

    //Convert input image into a int array of intensities.
    private int[][] BImagetoIntArr(BufferedImage image)
    {
        int[][] newRep = new int[image.getHeight()][image.getWidth()];

        for (int y=0; y<image.getHeight();y++)
        {
            for ( int x=0; x<image.getWidth();x++)
            {
                newRep[y][x] = image.getRGB(x,y) &0xffffff;
            }
        }

        return newRep;
    }

    //Convert int array into BufferedImage -> for Debugging
    private BufferedImage IntArrToBImage(int[][] input)
    {
        BufferedImage output= new BufferedImage(input[0].length, input.length, BufferedImage.TYPE_INT_RGB);

        for ( int y=0; y<input.length; y++)
        {
            for (int x=0; x<input[0].length;x++)
            {
                output.setRGB(x,y,input[y][x]);
            }
        }
        return output;
    }
}
