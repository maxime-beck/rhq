/**
 * @(#)JHistogram.java  1.0  January 27, 2008
 *
 * Copyright (c) 2008 Werner Randelshofer
 * Staldenmattweg 2, CH-6405 Immensee, Switzerland
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Werner Randelshofer. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Werner Randelshofer.
 */
package ch.randelshofer.tree.demo;

import ch.randelshofer.tree.Colorizer;
import ch.randelshofer.tree.Weighter;
import java.awt.*;
import java.util.Arrays;

/**
 * JHistogram.
 *
 * @author Werner Randelshofer
 * @version 1.0 JHistogram Created.
 */
public class JHistogram extends javax.swing.JComponent {

    private Colorizer colorizer;
    private Weighter weighter;
    private int[] histogram;

    /** Creates new form. */
    public JHistogram() {
        initComponents();
    }

    public void setWeighter(Weighter newValue) {
        this.weighter = newValue;
        if (newValue != null) {
            histogram = weighter.getHistogram();
        } else {
            histogram = null;
        }
    }

    public void setColorizer(Colorizer newValue) {
        this.colorizer = newValue;
    }

    public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;

        Insets insets = getInsets();

        if (histogram != null) {
            drawHistogram(g, new Rectangle(insets.left, insets.top,
                    getWidth() - insets.left - insets.right,
                    getHeight() - insets.top - insets.bottom));
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(256, 40);
    }

    public void drawHistogram(Graphics2D g, Rectangle bounds) {
        if (colorizer != null) {
            for (int i = 0; i < bounds.width; i++) {
                float value = i / (float) (bounds.width);
                int x = bounds.x + i;
                g.setColor(colorizer.get(value));
                g.fillRect(x, bounds.y, 1, bounds.height);
            }
        }
        int maxCount = 0;
        for (int i = 0; i < histogram.length; i++) {
            maxCount = Math.max(maxCount, histogram[i]);
        }
        float scaleFactor = bounds.height / (float) maxCount;
        g.setColor(Color.black);
        for (int i = 0; i < histogram.length; i++) {
            /*if (i != 0) {
                System.out.print(',');
            }*/
            float value = i / (float) (histogram.length);
            int x = (int) (bounds.x + bounds.width * value);
            int height = (int) Math.ceil(scaleFactor * histogram[i]);
            //System.out.print(histogram[i] + " " + height);
            g.fillRect(x, bounds.y + bounds.height - height, 1, height);

        }
        //System.out.println();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
