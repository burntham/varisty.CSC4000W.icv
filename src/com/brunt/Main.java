package com.brunt;

import com.brunt.ImageProcessing.ImageManager;
import com.brunt.Viewer.Window;

import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String imageName = (args.length>0)? args[0] : input.nextLine();

        BufferedImage originalImage = ImageManager.ReadImage(args[0]);

        Window displayBox = new Window(imageName);
        displayBox.setImage(originalImage);

        if (originalImage == null)
        {
            System.out.printf("Image %s failed to load", args[0]);
        }
    }
}
