package org.opencalc.math;

import org.opencalc.tree.Node;

import java.util.ArrayList;
import java.util.Dictionary;

public class Term {

    String problem;
    public boolean hasVars = false;

    Node root;

    public Term(String problem){
        this.problem = prepareTerm(problem);
        //TODO: set hasVars if there are variables in the Term
        createTree();
        if(hasVars){ shortenTree(); }
    }

    public void setTerm(String problem){
        this.problem = prepareTerm(problem);
        //TODO: set hasVars if there are variables in the Term
        createTree();
        if(hasVars){ shortenTree(); }
    }

    //TODO: Prepare the given statement (eg. replace sqrt with √)
    String prepareTerm(String problem){
        return problem;
    }

    void createTree(){
        root = new Node(problem);
    }

    void shortenTree(){
        root.shortenTree();
    }

    public double solve(ArrayList<Character> keys, ArrayList<Double> values){
        return  root.solve(keys, values);
    }
}
