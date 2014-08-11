package com.brunt;

import com.brunt.ImageProcessing.Discs;
import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilterConvolution;
import com.brunt.ImageProcessing.Filters2.GaussianFilter2;
import com.brunt.ImageProcessing.HoughTransform;
import com.brunt.ImageProcessing.ImageManager;
import com.brunt.ImageProcessing.Utils;
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


        //Run a Gaussian Filter with a threshold
        GaussianFilterConvolution gaussian = new GaussianFilterConvolution(1.4f,2);
        BufferedImage gFilteredImage = Utils.convertIntArrToBufferedImage(gaussian.FilterImage(originalImage));
        displayBox.AddImage(gFilteredImage);

        int[] test = Utils.createIntArrayFromImg(originalImage);

       // Test new Gaussian Filter
        GaussianFilter2 newG = new GaussianFilter2(1.4f,2);
        int[] gaussed = newG.filterImage(Utils.createIntArrayFromImg(originalImage),originalImage.getWidth(),originalImage.getHeight());
        BufferedImage newGaussed = Utils.getGreyScaleBufferedImage(gaussed,originalImage.getWidth(),originalImage.getHeight());
        displayBox.AddImage(newGaussed);

        SobelFilterConvolution sobel = new SobelFilterConvolution();
        BufferedImage sobelOperatedImage = Utils.convertIntArrToBufferedImage(sobel.FilterImage(gFilteredImage));
        displayBox.AddImage(sobelOperatedImage);

        int[] range={5,60};
        HoughTransform hough = new HoughTransform(originalImage,range,1.4f,2,0);
        LinkedList<Discs> DiscList = hough.detectDiscs();
        BufferedImage houghTest = hough.drawAccumulator();
        displayBox.AddImage(houghTest);

        BufferedImage detected = new BufferedImage(originalImage.getWidth(),originalImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D gDet = detected.createGraphics();
        gDet.drawImage(originalImage,null,null);
        gDet.setColor(Color.red);
        Iterator<Discs> discIterator = DiscList.iterator();
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


        String.format("%d Discs discovered",DiscList.size());
        System.out.println(String.format("%d Discs discovered",DiscList.size()));








        displayBox.ShowWindow();

        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
