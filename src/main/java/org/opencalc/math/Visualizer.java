package org.opencalc.math;

import org.opencalc.tree.Node;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Visualizer extends Frame
{
    //AVL-Baum
    Node root;

    //For painting
    Graphics g;
    BufferedImage img;

    public Visualizer(int height, int width, Node root){
        super("AVL-Baum");

        //AVL-Baum
        this.root = root;

        //Frameconfiguration
        setSize(width, height);
        Dimension dss = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dss.width / 2 - width / 2;
        int y = dss.height / 2 - height / 2;
        setLocation(x, y);

        setResizable(true);
        setVisible(true);

        Dimension dbi = getSize();
        img = new BufferedImage( dbi.width, dbi.height, BufferedImage.TYPE_INT_RGB );
        g = img.getGraphics();

        drawTree();

        //X closes the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                dispose();
            }
        });

        //when the window is resized the drawTree method is called
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                drawTree();
            }
        });
    }

    //Method overrides super.paint method so that it is called by repaint()
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(img, 0, 0, this);
    }

    void setRoot(Node root){
        this.root = root;
    }

    public void drawTree(){
        //is needed because else Image is limited to width and height from creation
        Dimension dbi = getSize();
        img = new BufferedImage( dbi.width, dbi.height, BufferedImage.TYPE_INT_RGB );
        g = img.getGraphics();

        //Creation of testbackground
        g.setColor(new Color(220,220,230));
        g.fillRect(0,0, getSize().width, getSize().height);

        //draw Nodes
        if(root != null){
            drawNode(root, 1, getDepth(root, 1), 1);
        }

        this.repaint();
    }

    //to calculate the position the layer where the current node is on, the overall number of nodes and the index is needed
    void drawNode(Node node, int layer, int layers, int index){
        //this is used to get the number of nodes per layer
        int potence = 1;
        for(int i = 0; i < layer - 1; i++){
            potence *= 2;
        }

        //drawing the left and right nodes
        g.setColor(new Color(0,0,0));
        if(node.left != null){
            g.drawLine(getSize().width / (potence + 1) * index +10, getSize().height / (layers + 1) * layer +10, getSize().width / (potence*2 + 1) * (index*2-1) +10, getSize().height / (layers + 1) * (layer+1) +10);
            drawNode(node.left, layer + 1, layers, index * 2 -1);
        }
        g.setColor(new Color(0,0,0));
        if(node.right != null){
            g.drawLine(getSize().width / (potence + 1) * index +10, getSize().height / (layers + 1) * layer +10, getSize().width / (potence*2 + 1) * (index*2) +10, getSize().height / (layers + 1) * (layer+1) +10);
            drawNode(node.right, layer + 1, layers, index * 2);
        }

        //the node gets drawn last so that it is over the connecting lines
        g.setColor(new Color(100,100,100));
        g.fillArc(getSize().width / (potence + 1) * index, getSize().height / (layers + 1) * layer, 20, 20, 0, 360);
        g.setColor(new Color(0,0,0));
        g.drawArc(getSize().width / (potence + 1) * index, getSize().height / (layers + 1) * layer, 20, 20, 0, 360);
        //the value of the node is drawn
        g.setColor(new Color(0,125,255));
        g.setFont(new Font("Arial", Font.BOLD,12));
        //g.drawString(node.value + "; " + node.bf + "; " + node.height, getSize().width / (potence + 1) * index, getSize().height / (layers + 1) * layer);
        switch(node.type){
            case NUMBER -> {
                g.drawString((node.value == Math.E ? "e" : node.value) + "", getSize().width / (potence + 1) * index, getSize().height / (layers + 1) * layer);
            }
            case VARIABLE -> {
                g.drawString(node.symbol + "", getSize().width / (potence + 1) * index, getSize().height / (layers + 1) * layer);
            }
            default -> {
                g.drawString(node.type.toString() + "", getSize().width / (potence + 1) * index, getSize().height / (layers + 1) * layer);
            }
        }
    }

    int getDepth(Node node, int currentDepth){
        int dl = node.left != null ? getDepth(node.left, currentDepth + 1) : 0;
        int dr = node.right != null ? getDepth(node.right, currentDepth + 1) : 0;

        if(dl == 0 && dr == 0){
            return currentDepth;
        }
        return dl > dr? dl : dr;
    }
}
