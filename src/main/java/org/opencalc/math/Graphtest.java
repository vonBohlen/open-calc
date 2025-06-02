package org.opencalc.math;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Graphtest extends Frame implements Runnable{

    boolean run;
    int ups = 10;

    KeyHandler keyHandler = new KeyHandler();

    BufferedImage img;
    Graphics g;

    double xMin = -100;
    double xMax = 10;
    double yMin = -100;
    double yMax = 100;

    Term term;

    public Graphtest(int height, int width, boolean startMaxSize, Term term){
        super("Graphvisualisierer");

        //Frameconfiguration
        if(startMaxSize) {
            setExtendedState(MAXIMIZED_BOTH);
        }
        
        setSize(width, height);
        setResizable(true);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width / 2 - width / 2;
        int y = screen.height / 2 - height / 2;
        setLocation(x, y);

        setVisible(true);

        this.term = term;

        drawGraph();

        //X closes the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                run = false;
                dispose();
            }
        });

        //when the window is resized the drawTree method is called
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                drawGraph();
            }
        });

        addKeyListener(keyHandler);
    }

    void drawGraph(){

        Dimension dbi = getSize();
        img = new BufferedImage( dbi.width, dbi.height, BufferedImage.TYPE_INT_RGB );
        g = img.getGraphics();

        //Background
        g.setColor(new Color(220,220,230));
        g.fillRect(0,0, getSize().width, getSize().height);

        //draw x-Axis
        g.setColor(new Color(0,0,0));
        if(0 >= yMin && 0 <= yMax){
            double y = dbi.height-(-yMin)/(yMax-yMin)*dbi.height;
            g.drawLine(0, (int)y, dbi.width, (int)y);
        }
        //draw y-Axis
        g.setColor(new Color(0,0,0));
        if(0 >= xMin && 0 <= xMax){
            double x = (-xMin)/(xMax-xMin)*dbi.width;
            g.drawLine((int)x, 0, (int)x, dbi.height);
        }

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
            else{
                points.add(null);
            }
        }

        //draw points as lines because only points would have margins when the gradient is too high
        g.setColor(new Color(50,50,230));
        for(int i = 0; i < points.size()-1; i++){
            Dimension a = points.get(i);
            Dimension b = points.get(i+1);
            if(a != null && b != null){
                g.drawLine(a.width, a.height, b.width, b.height);
            }
        }

        this.repaint();
    }



    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(img, 0, 0, this);
    }

    @Override
    public void run() {
        run = true;
        double updateInterval = 1000000000 / (double)ups;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(run){
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / updateInterval;
            lastTime = currentTime;

            if(delta >= 1){
                System.out.println("HI");
                if(action()){
                    drawGraph();
                }
                delta--;
            }
        }
    }

    boolean action(){
        boolean updated = false;
        //zoom in
        if(keyHandler.PLUS_KEY && !keyHandler.MINUS_KEY){
            //x-Axis 20% in
            double x = (xMax-xMin)*0.2;
            xMin += x;
            xMax -= x;
            //y-Axis 20% in
            double y = (yMax-yMin)*0.2;
            yMin += y;
            yMax -= y;

            updated = true;
        }
        //zoom out
        if(keyHandler.MINUS_KEY && !keyHandler.PLUS_KEY){
            //x-Axis 20% out
            double x = (xMax-xMin)*0.2;
            xMin -= x;
            xMax += x;
            //y-Axis 20% out
            double y = (yMax-yMin)*0.2;
            yMin -= y;
            yMax += y;

            updated = true;
        }
        //move up
        if(keyHandler.W_KEY && !keyHandler.S_KEY){
            //y-Axis 20% up
            double y = (yMax-yMin)*0.2;
            yMin += y;
            yMax += y;

            updated = true;
        }
        //move down
        if(keyHandler.S_KEY && !keyHandler.W_KEY){
            //y-Axis 20% down
            double y = (yMax-yMin)*0.2;
            yMin -= y;
            yMax -= y;

            updated = true;
        }
        //move left
        if(keyHandler.A_KEY && !keyHandler.D_KEY){
            //x-Axis 20% left
            double x = (xMax-xMin)*0.2;
            xMin -= x;
            xMax -= x;

            updated = true;
        }
        //move right
        if(keyHandler.D_KEY && !keyHandler.A_KEY){
            //x-Axis 20% right
            double x = (xMax-xMin)*0.2;
            xMin += x;
            xMax += x;

            updated = true;
        }

        return updated;
    }
}
