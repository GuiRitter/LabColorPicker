package io.github.guiritter.labcolorpicker;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * An extension of {@link java.awt.Canvas Canvas}
 * that allows the direct manipulation of pixels.
 * @author Guilherme Ritter
 */
public class WritableCanvas extends Canvas {
    
    /**
     * Iterator.
     */
    private int fa = 0; 
    
    /**
     * Iterator.
     */
    private int fb = 0;
    
    private BufferedImage image;
    private WritableRaster raster;
    
    /**
     * Initializes the canvas.
     * @param width the canvas' width
     * @param height the canvas' height
     */
    public WritableCanvas(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        raster = image.getRaster();
    }
    
    /**
     * Calls the {@link #setPixel(int,int,int[]) setPixel}
     * method for all of this canvas' pixels, then calls this object's
     * {@link #repaint() repaint}.
     * This method reads the canvas' witdh and height from the values set by
     * {@link #setBounds(int,int,int,int) setBounds}.
     * @param color <code>int</code> array containing,
     * in this order, the values for R, G and B
     */
    public void fill (int color[]) {
        for (fa = 0; fa < getSize().width; fa++) {
            for (fb = 0; fb < getSize().height; fb++) {
                raster.setPixel(fa, fb, color);
            }
        }
        repaint();
    }
    
    /**
     * Paints this canvas. This is a necessary override,
     * since this class extends {@link java.awt.Canvas Canvas}.
     * Not necessary to be used by the programmer.
     * @param g the specified {@link java.awt.Graphics Graphics} context
     */
    @Override
    public void paint (Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
    
    /**
     * Sets the given pixel's color. 
     * @param x the pixel's horizontal coordinate
     * @param y the pixel's vertical coordinate
     * @param color <code>int</code> array containing,
     * in this order, the values for R, G and B
     */
    public void setPixel (int x, int y, int color[]) {
        raster.setPixel(x, y, color);
    }
}
