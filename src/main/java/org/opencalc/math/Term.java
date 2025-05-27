package org.opencalc.math;

import org.opencalc.tree.Node;

import java.util.ArrayList;
import java.util.Dictionary;

public class Term {

    String problem;
    public boolean hasVars;

    Node root;

    public Term(String problem){
        this.problem = problem;
        //TODO: set hasVars if there are variables in the Term
        createTree();
        if(hasVars){ shortenTree(); }
    }

    public void setTerm(String problem){
        this.problem = problem;
        //TODO: set hasVars if there are variables in the Term
        createTree();
        if(hasVars){ shortenTree(); }
    }

    void createTree(){

    }

    void shortenTree(){
        root.shortenTree();
    }

    public double solve(Dictionary<Character, Double> assignation){
        return root.solve(assignation);
    }
}
