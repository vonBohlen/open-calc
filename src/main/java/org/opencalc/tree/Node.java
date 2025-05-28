package org.opencalc.tree;

import java.util.ArrayList;
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
        int additionalBrackets = 0;



        boolean splitter = false;
        //only repeats if the whole term is bracketed
        while(!splitter) {
            indexLPO = 0;
            hierarchy = 10;

            ignore = false;
            additionalBrackets = 0;

            //determines the splitting point
            for (int i = 0; i < term.length(); i++) {
                if (ignore) {
                    if(term.charAt(i) == '('){
                        additionalBrackets++;
                        continue;
                    }
                    if (term.charAt(i) == ')') {
                        if(additionalBrackets == 0){
                            ignore = false;
                        }
                        else{
                            additionalBrackets--;
                        }
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
                    case '^', '?' -> {
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
                //account for e
                if(term.charAt(0) == 'e'){
                    type = Types.NUMBER;
                    value = Math.E;
                    return;
                }
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
                boolean leftS = left.shortenTree();
                boolean rightS = right.shortenTree();
                if(leftS && rightS){
                    value = left.value + right.value;
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case SUBTRACT -> {
                boolean leftS = left.shortenTree();
                boolean rightS = right.shortenTree();
                if(leftS && rightS){
                    value = left.value - right.value;
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case MULTIPLY -> {
                boolean leftS = left.shortenTree();
                boolean rightS = right.shortenTree();
                if(leftS && rightS){
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
                boolean leftS = left.shortenTree();
                boolean rightS = right.shortenTree();
                if(leftS && rightS){
                    value = Math.pow(left.value, right.value);
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case ROOT -> {
                if(left.shortenTree()){
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
    public double solve(ArrayList<Character> keys, ArrayList<Double> values){
        switch(type){
            case VARIABLE -> {
                return values.get(keys.indexOf(symbol));
            }
            case NUMBER -> { return value; }
            case ADD -> {
                return left.solve(keys, values) + right.solve(keys, values);
            }
            case SUBTRACT -> {
                return left.solve(keys, values) - right.solve(keys, values);
            }
            case MULTIPLY -> {
                return left.solve(keys, values) * right.solve(keys, values);
            }
            case DIVIDE -> {
                return left.solve(keys, values) / right.solve(keys, values);
            }
            case POTENCY -> {
                return Math.pow(left.solve(keys, values), right.solve(keys, values));
            }
            case ROOT -> {
                return Math.sqrt(left.solve(keys, values));
            }
            default -> { return 0.0; }
        }
    }
}
