package org.opencalc.tree;

import java.util.Dictionary;

public class Node {

    public Types type;

    public Node left;
    public Node right;

    public double value;
    public char symbol;

    public Node(String term){
        int indexLPO = 0;
        int hierarchy = 10;

        boolean ignore = false;



        boolean splitter = true;
        //only repeats if the whole term is bracketed
        while(!splitter) {
            //determines the splitting point
            for (int i = 0; i < term.length(); i++) {
                if (ignore) {
                    if (term.charAt(i) == ')') {
                        ignore = false;
                    }
                    continue;
                }
                switch (term.charAt(i)) {
                    //1
                    case '+', '-' -> {
                        if (hierarchy > 1) {
                            hierarchy = 1;
                            indexLPO = i;
                        }
                    }
                    //2
                    case '*', '/' -> {
                        if (hierarchy > 2) {
                            hierarchy = 2;
                            indexLPO = i;
                        }
                    }
                    //3
                    case '^', '√' -> {
                        if (hierarchy > 3) {
                            hierarchy = 3;
                            indexLPO = i;
                        }
                    }
                    //4
                    case '(' -> {
                        if (hierarchy > 4) {
                            hierarchy = 4;
                            indexLPO = i;
                        }
                        ignore = true;
                    }
                }
            }

            //checks if the whole term is bracketed, erases them if so and repeats the process
            if(hierarchy == 4){
                term = term.substring(1,term.length()-1);
                splitter = false;
            }
            else{
                splitter = true;
            }
        }

        //if there was no operand in the term it means that it was either a number or a variable
        if(hierarchy == 10){
            double num;
            try{
                num = Double.parseDouble(term);
            }catch (Exception e){
                type = Types.VARIABLE;
                symbol = term.charAt(0);
                return;
            }
            type = Types.NUMBER;
            value = num;
            return;
        }

        //if there was an operand the term gets split and the left and right child nodes get determined
        switch (hierarchy){
            case 1 -> {
                //get type
                type = term.charAt(indexLPO) == '+' ? Types.ADD : Types.SUBTRACT;
                //set left tree
                left = new Node(term.substring(0, indexLPO));
                //set right tree
                right = new Node(term.substring(indexLPO+1));
            }
            case 2 -> {
                //get type
                type = term.charAt(indexLPO) == '*' ? Types.MULTIPLY : Types.DIVIDE;
                //set left tree
                left = new Node(term.substring(0, indexLPO));
                //set right tree
                right = new Node(term.substring(indexLPO+1));
            }
            case 3 -> {
                //in case of a potency
                if(term.charAt(indexLPO) == '^'){
                    type = Types.POTENCY;
                    //set left tree
                    left = new Node(term.substring(0, indexLPO));
                    //set right tree
                    right = new Node(term.substring(indexLPO+1));
                }
                //in case of a square root
                else{
                    type = Types.ROOT;
                    //set left tree
                    left = new Node(term.substring(indexLPO+1));
                    //only the left tree is set
                }
            }
        }
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
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case SUBTRACT -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value - right.value;
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case MULTIPLY -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value * right.value;
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case DIVIDE -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = left.value / right.value;
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case POTENCY -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = Math.pow(left.value, right.value);
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case ROOT -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = Math.sqrt(left.value);
                    type = Types.NUMBER;

                    left = null;
                    right = null;

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
