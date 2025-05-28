package org.opencalc.math;

import java.beans.Visibility;

public class Test {

    public static void main(String[] args){
        Term term = new Term("?(2^(1+1))");
        Visualizer visualizer = new Visualizer(800, 800, term.root);
        System.out.println(term.solve(null));
    }
}
