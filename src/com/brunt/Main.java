package com.brunt;

import com.brunt.ImageProcessing.*;
import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilterConvolution;
import com.brunt.ImageProcessing.Filters2.CannyEdgeDetection;
import com.brunt.ImageProcessing.Filters2.GaussianFilter2;
import com.brunt.ImageProcessing.Filters2.SobelFilter2;
import com.brunt.Viewer.Window;
import com.sun.javafx.binding.StringFormatter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        /**
         * paramaters
         */
        float sigma = 1.4f;
        int gausRadius =2;
        int minCircleRadius = 10;
        int maxCircleRadius = 150;
        int lowT = 50;//still to be implemented
        int highT = 60;

        /**
         * Initialization
         */
        Scanner input = new Scanner(System.in);
        BufferedImage originalImage;
        int[] original;
        int width,height;
        String imageName=" ";

        imageName = (args.length > 0) ? args[0] : "";
        File file = new File(imageName);

        while((!file.canRead()))
        {
            System.out.println("Please enter a valid filename");
            imageName = input.nextLine();
            file = new File(imageName);
        }

        System.out.println("Please enter min radius");
        minCircleRadius = input.nextInt();

        System.out.println("Please enter max radius");
        maxCircleRadius = input.nextInt();


        originalImage = ImageManager.ReadImage(imageName);
        original = Utils.createIntArrayFromImg(originalImage);
        width = originalImage.getWidth();
        height = originalImage.getHeight();

        Window displayBox = new Window("");
        displayBox.AddImage(originalImage);

        /**
         * Display various operations
         */

        //Filters and operations to display different steps
        GaussianFilter2 gaussFilter = new GaussianFilter2(sigma,gausRadius);
        SobelFilter2 sobelOperator = new SobelFilter2();
        CannyEdgeDetection cannyDetection = new CannyEdgeDetection(lowT, highT);
        HoughTransform2 houghTransform = new HoughTransform2(minCircleRadius,maxCircleRadius,sigma,gausRadius);

        //step 1 Gaussian Blur
        int[] blurred = gaussFilter.filterImage(original,width,height);

        //step 2 Apply Sobel Operator -> note this is gradient magnitudes
        int[] sobelled = sobelOperator.filterImage(blurred,width,height);

        //step 3 Apply CannyEdge Detection (note, this filter implements the sobel operator, so the blurred data is passed instead)
        //This redoes step 1 and 2 aswell as a nonmaximum supression
        //still to implement hysteresis
        int edges[]=cannyDetection.detectEdges(original,width,height);

        //step 4 Apply Hough Transform
        LinkedList<Discs> detectedDiscs= houghTransform.detectDiscs(original,width,height,minCircleRadius,maxCircleRadius,sigma,gausRadius);


        BufferedImage detected = new BufferedImage(originalImage.getWidth(),originalImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D gDet = detected.createGraphics();
        gDet.drawImage(originalImage,null,null);
        gDet.setColor(Color.red);
        Iterator<Discs> discIterator = detectedDiscs.iterator();
        System.out.println(String.format("%d discs detected:",detectedDiscs.size()));
        while(discIterator.hasNext())
        {
            Discs testDisc = discIterator.next();
            int radius = testDisc.radius;
            int diameter = radius*2;
            int x=testDisc.x,y = testDisc.y;
            gDet.drawOval(x-radius,y-radius,diameter,diameter);
        }
        gDet.dispose();

        /**
         * Draw all the stages to the window!
         */

        displayBox.AddImage(Utils.getGreyScaleBufferedImage(blurred,width,height));
        displayBox.AddImage(Utils.getIntensityBufferedImage(sobelled,width,height));
        displayBox.AddImage(Utils.getGreyScaleBufferedImage(edges,width,height));
        displayBox.AddImage(houghTransform.drawAccumulator());
        displayBox.AddImage(detected);
        displayBox.ShowWindow();

        ImageManager.WriteImage(String.format("%s-detected.gif",imageName.split(".gif")[0]),detected);

        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
