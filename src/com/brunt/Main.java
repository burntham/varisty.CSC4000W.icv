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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String imageName = (args.length>0)? args[0] : input.nextLine();

        //create window
        Window displayBox = new Window(imageName);

        //Load the original Image
        BufferedImage originalImage = ImageManager.ReadImage(args[0]);
        displayBox.AddImage(originalImage);
        int[] original = Utils.createIntArrayFromImg(originalImage);
        int width = originalImage.getWidth(), height = originalImage.getHeight();

        GaussianFilter2 newG = new GaussianFilter2(1.4f,2);
        int[] gaussed = newG.filterImage(original,originalImage.getWidth(),originalImage.getHeight());
        BufferedImage newGaussed = Utils.getGreyScaleBufferedImage(gaussed,width,height);
        displayBox.AddImage(newGaussed);

        //Test New Sobel 2 Filter
        SobelFilter2 newS = new SobelFilter2();
        int[] sobelled = newS.filterImage(gaussed,originalImage.getWidth(), originalImage.getHeight());
        BufferedImage newSobelled = Utils.getGreyScaleBufferedImage(sobelled, originalImage.getWidth(), originalImage.getHeight());
        displayBox.AddImage(newSobelled);

        //Test New CannyEdgeDetector
        CannyEdgeDetection newCan = new CannyEdgeDetection(0,0);
        int[] cannied = newCan.detectEdges(original,originalImage.getWidth(), originalImage.getHeight() );
        BufferedImage newCannied = Utils.getRawColouredBufferedImage(cannied, originalImage.getWidth(), originalImage.getHeight());
        displayBox.AddImage(newCannied);

        HoughTransform2 newHough = new HoughTransform2(14,60,1.4f,2);
        LinkedList<Discs> newHoughDiscs = newHough.detectDiscs(original,width,height,14,16,1.4f,2);
        System.out.println(String.format("%d discs found with new Hough", newHoughDiscs.size()));
        BufferedImage newHoughSpace = newHough.drawAccumulator();

        displayBox.AddImage(newHoughSpace);


        BufferedImage detected = new BufferedImage(originalImage.getWidth(),originalImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D gDet = detected.createGraphics();
        gDet.drawImage(originalImage,null,null);
        gDet.setColor(Color.red);
        Iterator<Discs> discIterator = newHoughDiscs.iterator();
        while(discIterator.hasNext())
        {
            Discs testDisc = discIterator.next();
            int radius = testDisc.radius;
            int diameter = radius*2;
            int x=testDisc.x,y = testDisc.y;
            gDet.drawOval(x-radius,y-radius,diameter,diameter);
        }
        gDet.dispose();
        displayBox.AddImage(detected);

        displayBox.ShowWindow();

        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
