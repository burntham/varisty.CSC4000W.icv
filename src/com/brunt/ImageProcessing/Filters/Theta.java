package com.brunt.ImageProcessing.Filters;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Daniel on 8/10/2014.
 */
public class Theta extends Point {
    private int theta;
    public Theta(int x, int y, int theta) {
        super(x, y);
        this.theta = theta;
    }

    public int getTheta()
    {
        return theta;
    }
}
