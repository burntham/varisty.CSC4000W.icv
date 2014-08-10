package com.brunt;

import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilterConvolution;
import com.brunt.ImageProcessing.HoughTransform;
import com.brunt.ImageProcessing.ImageManager;
import com.brunt.ImageProcessing.Utils;
import com.brunt.Viewer.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String imageName = (args.length>0)? args[0] : input.nextLine();

        //Load the original Image
        BufferedImage originalImage = ImageManager.ReadImage(args[0]);

        //Run a Gaussian Filter with a threshold
        GaussianFilterConvolution gaussian = new GaussianFilterConvolution(1.4f,1, 30);
        BufferedImage gFilteredImage = Utils.convertIntArrToBufferedImage(gaussian.FilterImage(originalImage));

        SobelFilterConvolution sobel = new SobelFilterConvolution();
        BufferedImage sobelOperatedImage = Utils.convertIntArrToBufferedImage(sobel.FilterImage(gFilteredImage));

        int[] range={15,32};
        HoughTransform hough = new HoughTransform(originalImage,range,1.4f,1,30);
        hough.detectDiscs();
        BufferedImage houghTest = hough.drawAccumulator();
        //create window
        Window displayBox = new Window(imageName);

        //Add images to window Pane
        displayBox.AddImage(originalImage);
        displayBox.AddImage(gFilteredImage);
        displayBox.AddImage(sobelOperatedImage);
        displayBox.AddImage(houghTest);

        //display window
        displayBox.ShowWindow();

        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
