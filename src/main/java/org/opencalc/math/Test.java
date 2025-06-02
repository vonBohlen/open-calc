package org.opencalc.math;

import java.awt.*;
import java.beans.Visibility;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Test {

    public static void main(String[] args){

        //TODO: Wenn zum Beispiel -2x verwendet wird muss daraus beim preparen -2*x gemacht werden

        //String problem = "5*(-9+2*x*?(e^x))";
        //String problem = "";
        String problem = "0.001*x^5-0.01*x^4+9";

        Term term = new Term(problem);
        term.shortenTree();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Visualizer visualizer = new Visualizer(screen.height, screen.width, term.root);

        Graphtest test = new Graphtest(800, 800, term);


        /*
        HashMap<Character, Double> assignations = new HashMap<>();
        assignations.put('x', 2.0);

        for(int i = 0; i < 3; i++){
            assignations.put('x', (double)i);
            System.out.println(term.solve(assignations));
        }*/
        //System.out.println(term.solve(keys, values));
    }
}
