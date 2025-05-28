package org.opencalc.math;

import java.beans.Visibility;
import java.util.ArrayList;
import java.util.Dictionary;

public class Test {

    public static void main(String[] args){
        /*String problemP1 = "?(92*e^(17.3+5*";
        String problemP2 = "))*2";
        double x = 2;*/

        String problem = "x*?(92*e^(17.3-5*2)+4)";

        Term term = new Term(problem);
        term.shortenTree();
        Visualizer visualizer = new Visualizer(800, 800, term.root);

        ArrayList<Character> keys = new ArrayList<>();
        keys.add('x');
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);

        for(int i = 0; i < 3; i++){
            values.set(0, (double)i);
            System.out.println(term.solve(keys, values));
        }
        //System.out.println(term.solve(keys, values));
    }
}
