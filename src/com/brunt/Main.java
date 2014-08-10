package com.brunt;

import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilterConvolution;
import com.brunt.ImageProcessing.ImageManager;
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
        GaussianFilterConvolution gaussian = new GaussianFilterConvolution(1.4f,2,80);
        BufferedImage gFilteredImage = gaussian.FilterImage(originalImage);

        SobelFilterConvolution sobel = new SobelFilterConvolution();
        BufferedImage sobelOperatedImage = sobel.FilterImage(gFilteredImage);

        int test = gFilteredImage.getRGB(180,20)&0xffffff;
        for ( int i=31; i>=0; i--)
        {
            System.out.print(test>>i & 1);
        }
        System.out.println("\n"+new Color(test)+" "+ test);
        //create window
        Window displayBox = new Window(imageName);

        //Add images to window Pane
        displayBox.AddImage(originalImage);
        displayBox.AddImage(gFilteredImage);
        displayBox.AddImage(sobelOperatedImage);

        //display window
        displayBox.ShowWindow();

        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
