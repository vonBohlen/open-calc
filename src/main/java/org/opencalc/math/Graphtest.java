package org.opencalc.math;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Graphtest extends Frame {

    BufferedImage img;
    Graphics g;

    double xMin = 0;
    double xMax = 10;
    double yMin = 0;
    double yMax = 10;

    Term term;

    public Graphtest(int height, int width, Term term){
        super("Graphvisualisierer");

        //Frameconfiguration
        setSize(width, height);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width / 2 - width / 2;
        int y = screen.height / 2 - height / 2;
        setLocation(x, y);

        setResizable(true);
        setVisible(true);

        this.term = term;

        drawGraph();

        //X closes the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                dispose();
            }
        });

        //when the window is resized the drawTree method is called
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                drawGraph();
            }
        });
    }

    void drawGraph(){

        Dimension dbi = getSize();
        img = new BufferedImage( dbi.width, dbi.height, BufferedImage.TYPE_INT_RGB );
        g = img.getGraphics();

        //Background
        g.setColor(new Color(220,220,230));
        g.fillRect(0,0, getSize().width, getSize().height);

        //get all points
        HashMap<Character, Double> xValue = new HashMap();
        xValue.put('x', 0.0);

        ArrayList<Dimension> points = new ArrayList<>();

        for(int i = -1; i <= dbi.width; i++){
            //true x value
            double x = xMin + (i+0.5)/dbi.width*(xMax-xMin);
            //solve for x
            xValue.put('x', x);
            double result = term.solve(xValue);
            //if result is on screen
            if(result >= yMin && result <= yMax){
                //y coordinate on screen
                double y = (result - yMin)/(yMax-yMin)*dbi.height;
                //add to list but alternate y because the origins don't align
                points.add(new Dimension(i, dbi.height-(int)y));
            }
        }

        //draw points as lines because only points would have margins when the gradient is too high
        g.setColor(new Color(50,50,230));
        for(int i = 0; i < points.size()-1; i++){
            g.drawLine(points.get(i).width, points.get(i).height, points.get(i+1).width, points.get(i+1).height);
        }

        this.repaint();
    }

    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(img, 0, 0, this);
    }
}
