package com.brunt;

import com.brunt.ImageProcessing.Filters.Filter2DSeperable;
import com.brunt.ImageProcessing.Filters.GaussianFilter;
import com.brunt.ImageProcessing.ImageManager;
import com.brunt.Viewer.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String imageName = (args.length>0)? args[0] : input.nextLine();

        BufferedImage originalImage = ImageManager.ReadImage(args[0]);

        //Filter Test;
        float[] one = {0.25f,0.5f,0.25f};
        //Filter2DSeperable filterTest = new Filter2DSeperable(one,one);
        GaussianFilter GFilter = new GaussianFilter(4f,20);

        BufferedImage fitTest = GFilter.FilterImage(originalImage);


        Window displayBox = new Window(imageName);
        displayBox.setImage(originalImage);
        Window displayBox2 = new Window(imageName+"blurred");
        displayBox2.setImage(fitTest);

//        int testme = originalImage.getRGB(62,402);
//        System.out.println(new Color(testme));
//        System.out.println("alpha:"+(testme>>24^0xff)+" red:"+(testme>>16 & 0xff)+" Green:"+(testme>>8&0xff)+ " blue:"+(testme&0xff));


        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
