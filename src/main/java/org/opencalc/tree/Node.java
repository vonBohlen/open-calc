package org.opencalc.tree;

import java.util.Dictionary;

public class Node {

    public Types type;

    public Node left;
    public Node right;

    public double value;
    public char symbol;

    public Node(String term){

    }

    //shortens the tree by alternating its value based on its adjacent Nodes by calling this method on them and looking at their returned boolean
    //is called for faster handling with variables
    public boolean shortenTree(){
        switch(type){
            case VARIABLE -> { return false; }
            case NUMBER -> { return true; }
            case ADD -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value + right.value;
                    return true;
                }
                return false;
            }
            case SUBTRACT -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value - right.value;
                    return true;
                }
                return false;
            }
            case MULTIPLY -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value * right.value;
                    return true;
                }
                return false;
            }
            case DIVIDE -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value / right.value;
                    return true;
                }
                return false;
            }
            case POTENCY -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = Math.pow(left.value, right.value);
                    return true;
                }
                return false;
            }
            case ROOT -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = Math.sqrt(left.value);
                    return true;
                }
                return false;
            }
            default -> { return false; }
        }
    }

    //solves the tree without alternating it just by calling this method on its adjacent Nodes which return their results
    public double solve(Dictionary<Character, Double> assignations){
        switch(type){
            case VARIABLE -> {
                return assignations.get(symbol);
            }
            case NUMBER -> { return value; }
            case ADD -> {
                return left.solve(assignations) + right.solve(assignations);
            }
            case SUBTRACT -> {
                return left.solve(assignations) - right.solve(assignations);
            }
            case MULTIPLY -> {
                return left.solve(assignations) * right.solve(assignations);
            }
            case DIVIDE -> {
                return left.solve(assignations) / right.solve(assignations);
            }
            case POTENCY -> {
                return Math.pow(left.solve(assignations), right.solve(assignations));
            }
            case ROOT -> {
                return Math.sqrt(left.solve(assignations));
            }
            default -> { return 0.0; }
        }
    }
}
