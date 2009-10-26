/**
 * @(#)RectmapView.java  1.0  January 16, 2008
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
package ch.randelshofer.tree.rectmap;

import ch.randelshofer.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.LinkedList;
import javax.swing.*;

/**
 * RectmapView provides an interactive user interface for a {@link RectmapTree}.
 * <p>
 * Supports zooming into a subtree.
 *
 * @author Werner Randelshofer
 * @version 1.0 RectmapView Created.
 */
public class RectmapView extends javax.swing.JPanel {

    private RectmapDraw draw;
    private BufferedImage img;
    private boolean isInvalid;
    private Thread worker;
    private boolean drawHandles;
    private boolean isAdjusting;
    private boolean needsSimplify;
    private RectmapNode hoverNode;
    private NodeInfo info;


    /** Creates new form. */
    public RectmapView() {
    }

    public RectmapView(RectmapTree model) {
        this.info = model.getInfo();
        this.draw = new RectmapDraw(model.getRoot(), model.getInfo());
        init();
    }

    private void init() {
        initComponents();
        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        ToolTipManager.sharedInstance().registerComponent(this);
    //    setFont(new Font("Dialog", Font.PLAIN, 9));
    }
    /**
     * Returns the tooltip to be displayed.
     *
     * @param event    the event triggering the tooltip
     * @return         the String to be displayed
     */
    public String getToolTipText(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();

        RectmapNode node = draw.getNodeAt(x, y);
        return (node == null) ? null : info.getTooltip(node.getDataNodePath());
    }


    private void setDrawPosition(double cx, double cy) {
        draw.setX(cx);
        draw.setY(cy);
    }

    private void setDrawSize(double w, double h) {
        draw.setWidth(w);
        draw.setHeight(h);
    }

    public void paintComponent(Graphics gr) {
        int w = getWidth();
        int h = getHeight();
                setDrawPosition(2, 2);
                setDrawSize(w - 4, h - 4);

        if (img == null ||
                img.getWidth() != w ||
                img.getHeight() != h) {
            img = null;
            img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            isInvalid = true;
        }
        if (isInvalid) {
            isInvalid = false;
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setBackground(Color.WHITE);
            g.setFont(getFont());
            g.clearRect(0, 0, img.getWidth(), img.getHeight());
            g.setClip(new Rectangle(0,0,img.getWidth(),img.getHeight()));
            if (isAdjusting && needsSimplify) {
              //  draw.drawContours(g, draw.getRoot(), Color.gray);
                draw.drawTree(g);
            } else {
                long start = System.currentTimeMillis();
                draw.drawTree(g);
                long end = System.currentTimeMillis();
                needsSimplify = (end - start) > 500;
            }

            g.dispose();
        }


        if (worker == null) {
            gr.drawImage(img, 0, 0, this);
        }
        RectmapNode selectedNode = draw.getDrawRoot();
        if (selectedNode != null) {
            Graphics2D g = (Graphics2D) gr;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
           // draw.drawNodeBounds(g, selectedNode, Color.blue);
        }
        if (hoverNode != null) {
            Graphics2D g = (Graphics2D) gr;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            RectmapNode node = hoverNode;
            while ((node = node.getParent()) != null) {
            draw.drawNodeBounds(g, node, Color.blue);
            }
            draw.drawNodeBounds(g, hoverNode, Color.red);
        }

        if (drawHandles) {
            Graphics2D g = (Graphics2D) gr;
            double cx = draw.getX();
            double cy = draw.getY();
            g.setColor(Color.BLACK);
            AffineTransform t = new AffineTransform();
            t.translate(cx, cy);
          //  t.rotate(draw.getRotation() * Math.PI / -180d);
            AffineTransform oldT = (AffineTransform) g.getTransform().clone();
            g.setTransform(t);
            g.draw(new Line2D.Double(-5, 0, 5, 0));
            g.draw(new Line2D.Double(0, -5, 0, 5));
            g.setTransform(oldT);
        }

    }

    private class MouseHandler implements MouseListener, MouseMotionListener {

        public void mouseClicked(MouseEvent evt) {
            RectmapNode node = draw.getNodeAt(evt.getX(), evt.getY());
            if (node != draw.getDrawRoot() || node == null && draw.getDrawRoot() != draw.getRoot()) {
            draw.setDrawRoot((node == null || node == draw.getDrawRoot()) ? draw.getRoot() : node);
            }

            isInvalid = true;
            repaint();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent evt) {
            hoverNode = draw.getNodeAt(evt.getX(), evt.getY());
            repaint();
        }

        public void mouseExited(MouseEvent e) {
            hoverNode = null;
            repaint();
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent evt) {
            hoverNode = draw.getNodeAt(evt.getX(), evt.getY());
            repaint();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
