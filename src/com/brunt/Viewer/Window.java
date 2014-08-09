package com.brunt.Viewer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/6/2014.
 */
public class Window extends JFrame {

    public Window(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    }

    public void setImage(BufferedImage img)
    {
        //convert image to label:
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        //add image to frame
        add(imageLabel);

        //resize frame to fit contents and reposition before displaying
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width - this.getWidth())/2, (dim.height - this.getHeight())/2);
        setVisible(true);
        //setResizable(false);
    }
}
