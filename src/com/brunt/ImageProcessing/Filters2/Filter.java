package com.brunt.ImageProcessing.Filters2;

/**
 * Created by Daniel on 8/11/2014.
 */
public abstract class Filter {

    /**
     * Set call filterImage to create a new filtererd int[] dataset,
     * @return
     */
    public abstract int[] filterImage(int[] original, int width, int height);

    protected int[] filter1DConvolution(int[] input,int width,int height, float[] filter)
    {
        int[] filtered = new int[width*height];
        int offset = filter.length/2;
        int columns = width;
        int rows = height;

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
                    newValue += (int)input[y*width+index]*filter[j];
                }
                filtered[y*width+x] = (int)newValue;
            }
        }
        return filtered;
    }


//    /**
//     * Do a 1D Convolution by rows and output to an existing array
//     * @param input
//     * @param output
//     * @param width
//     * @param height
//     * @param filter
//     */
//    private void filter1DConvolution(int[] input,int[] output, int width,int height, float[] filter)
//    {
//        int offset = filter.length/2;
//        int columns = width;
//        int rows = height;
//
//        for ( int y = 0; y<rows; y++)
//        {
//            for (int x = 0; x <columns; x++)
//            {
//                float newValue = 0.0f;
//
//                for ( int j =0; j<filter.length;j++)
//                {
//                    int index=0;
//                    if ( x- offset+j <0)
//                        index=0;
//                    else if(x-offset+j>=columns)
//                        index = columns-1;
//                    else
//                        index = x-offset+j;
//                    newValue += (int)input[y*width+index]*filter[j];
//                }
//                output[y*width+x] = (int)newValue;
//            }
//        }
//    }

    protected int[] filter2DSeperableConvolution(int[] inputArr, int width, int height, float[] filterX, float[] filterY)
    {
        int[] outputArr = new int[width*height];
        //Filter by row
        outputArr = filter1DConvolution(inputArr,width,height,filterX);

        //Transpose and filter by row (effectively filtering columns
        transposeArr(outputArr, outputArr,width,height);
        outputArr = filter1DConvolution(inputArr,height,width,filterY);
        return outputArr;
    }

    /**
     * transposeArr will output a transposed array to outputArr if the argument is passed
     * @param width
     * @param height
     * @param inputArr
     * @param outputArr
     */
    private void transposeArr(int[] inputArr, int[] outputArr, int width, int height )
    {
        for ( int y=0;y<height;y++)
        {
            for (int x=0;x<width;x++)
            {
                if(inputArr!=outputArr)
                    outputArr[x*height+y] = inputArr[y*width+x];
                else
                {
                    int temp = inputArr[y*width+x];
                    inputArr[y*width+x]=outputArr[x*height+y];
                    inputArr[y*width+x]= outputArr[x*height+y];
                }
            }
        }
    }
}
