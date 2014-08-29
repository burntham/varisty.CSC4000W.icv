package com.brunt.Viewer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Daniel on 8/6/2014.
 */
public class Window extends JFrame {

    private JPanel imagePanel;

    public Window(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        imagePanel = new JPanel();
        //imagePanel.setLayout(new FlowLayout(FlowLayout.TRAILING,0,0));
        add(imagePanel);
    }

    public void AddImage(BufferedImage img)
    {
        //convert image to label:
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imagePanel.add(imageLabel);

    }

    public void ShowWindow()
    {
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width - this.getWidth())/2, (dim.height - this.getHeight())/2);
        setVisible(true);
    }
}
