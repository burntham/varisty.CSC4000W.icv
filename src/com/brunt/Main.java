package com.brunt;

import com.brunt.ImageProcessing.Filters.GaussianFilterConvolution;
import com.brunt.ImageProcessing.Filters.SobelFilter;
import com.brunt.ImageProcessing.ImageManager;
import com.brunt.Viewer.Window;

import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String imageName = (args.length>0)? args[0] : input.nextLine();

        //Load the original Image
        BufferedImage originalImage = ImageManager.ReadImage(args[0]);

        //Run a Gaussian Filter with a threshold
        GaussianFilterConvolution gaussian = new GaussianFilterConvolution(1.4f,2, 30);
        BufferedImage gFilteredImage = gaussian.FilterImage(originalImage);

        SobelFilter sobel = new SobelFilter();
        BufferedImage sobelOperatedImage = sobel.FilterImage(gFilteredImage);

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
