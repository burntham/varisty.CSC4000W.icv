package com.brunt.ImageProcessing;

import java.awt.*;

/**
 * Created by Daniel on 8/10/2014.
 * Stores Gradient Angles
 */
public class Theta extends Point {
    private int theta;

    public Theta(int x, int y, int theta) {
        super(x, y);
        this.theta = theta;
    }

    public Theta(int x, int y, int gX, int gY) {
        super(x, y);
        theta = calculateTheta(gX,gY);
    }

    public int getTheta()
    {
        return theta;
    }

    /**
     * Calculate values for Theta
     * @param gX Gradient in X
     * @param gY Gradient in Y
     * @return Returns a value for theta in one of four quadrants
     */
    private int calculateTheta(int gX, int gY)
    {
        double temp = 0.0f;
        int theta;
        double angleTheta =Math.abs(Math.atan2(gX,gY));
        angleTheta = (angleTheta>180)? angleTheta-180:angleTheta;

        //Group Theta into a 'block' either 0,45,90 or 135 - to be used for Non-Maximum Supression
        if ( angleTheta <= 22.5 || angleTheta >= 157.5)
            theta =0;
        else if ( angleTheta <=67.5)
            theta = 45;
        else if(angleTheta<=112.5)
            theta =90;
        else
            theta = 135;

        return theta;
    }
}


