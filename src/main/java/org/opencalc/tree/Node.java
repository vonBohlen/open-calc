package org.opencalc.tree;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Node {

    public ArrayList<Character> operands = new ArrayList<>();

    public Types type;

    public Node left;
    public Node right;

    public double value;
    public char symbol;
    public boolean negative;

    public Node(String term){
        operands.add('+');
        operands.add('-');
        operands.add('*');
        operands.add('/');
        operands.add(')');

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
                    case '+' -> {
                        if (hierarchy > 1) {
                            hierarchy = 1;
                            indexLPO = i;
                        }
                    }
                    case '-' -> {
                        //if the symbol is at the first place or after an operand it is not an operand
                        if(i == 0 || operands.contains(term.charAt(i-1))){
                            continue;
                        }

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
                    case '^', '?', '~' -> {
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
                if(term.charAt(0) == '-'){
                    term = term.substring(1,term.length());
                    negative = !negative;
                    splitter = false;
                    continue;
                }
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
                if(term.charAt(0) == '-'){
                    symbol = term.charAt(1);
                    negative = true;
                    return;
                }
                symbol = term.charAt(0);
                negative = false;
                return;
            }
            type = Types.NUMBER;
            value = num;
            return;
        }

        //if there was an operand the term gets split and the left and right child nodes get determined
        switch (hierarchy){
            case 1 -> {
                if(term.charAt(indexLPO) == '+'){
                    type = Types.ADD;
                    //set left tree
                    left = new Node(term.substring(0, indexLPO));
                    //set right tree
                    right = new Node(term.substring(indexLPO+1));
                }
                //if the term is split all the add and sub operands have to get reversed because the calculation is handled as if there were brackets added
                else{
                    type = Types.SUBTRACT;
                    //set left tree
                    left = new Node(term.substring(0, indexLPO));
                    //set right tree
                    right = new Node(reverseTerm(term.substring(indexLPO+1)));
                }
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
                    type = term.charAt(indexLPO) == '?' ? Types.ROOT : Types.SINE;
                    //set left tree
                    left = new Node(term.substring(indexLPO+1));
                    //only the left tree is set
                }
            }
        }
    }

    String reverseTerm(String sub){
        boolean ignore = false;
        int additionalBrackets = 0;
        for(int i = 0; i < sub.length(); i++){
            if (ignore) {
                if(sub.charAt(i) == '('){
                    additionalBrackets++;
                    continue;
                }
                if (sub.charAt(i) == ')') {
                    if(additionalBrackets == 0){
                        ignore = false;
                    }
                    else{
                        additionalBrackets--;
                    }
                }
                continue;
            }
            if(sub.charAt(i) == '('){ ignore = true; }

            //switch pluses
            if(sub.charAt(i) == '+'){
                sub = sub.substring(0, i) + '-' + sub.substring(i+1);
                continue;
            }
            if(sub.charAt(i) == '-'){
                //if the symbol is at the first place or after an operand it is not an operand
                if(i == 0 || operands.contains(sub.charAt(i-1))){
                    continue;
                }

                //if the program runs till here the - is an operand
                sub = sub.substring(0, i) + '+' + sub.substring(i+1);
            }
        }
        return sub;
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
                    value = (negative ? -1 : 1) * left.value + right.value;
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
                    value = (negative ? -1 : 1) * left.value - right.value;
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
                    value = (negative ? -1 : 1) * left.value * right.value;
                    type = Types.NUMBER;

                    left = null;
                    right = null;

                    return true;
                }
                return false;
            }
            case DIVIDE -> {
                if(left.shortenTree() && right.shortenTree()){
                    value = (negative ? -1 : 1) * left.value / right.value;
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
                    value = (negative ? -1 : 1) * Math.pow(left.value, right.value);
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
            case SINE -> {
                if(left.shortenTree()){
                    value = Math.sin(left.value);
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
    public double solve(HashMap<Character, Double> assignation){
        switch(type){
            case VARIABLE -> {
                return negative ? -1*assignation.get(symbol) : assignation.get(symbol);
            }
            case NUMBER -> { return value; }
            case ADD -> {
                return (negative ? -1 : 1) * left.solve(assignation) + right.solve(assignation);
            }
            case SUBTRACT -> {
                return (negative ? -1 : 1) * left.solve(assignation) - right.solve(assignation);
            }
            case MULTIPLY -> {
                return (negative ? -1 : 1) * left.solve(assignation) * right.solve(assignation);
            }
            case DIVIDE -> {
                return (negative ? -1 : 1) * left.solve(assignation) / right.solve(assignation);
            }
            case POTENCY -> {
                return (negative ? -1 : 1) * Math.pow(left.solve(assignation), right.solve(assignation));
            }
            case ROOT -> {
                return (negative ? -1 : 1) * Math.sqrt(left.solve(assignation));
            }
            case SINE -> {
                return (negative ? -1 : 1) * Math.sin(left.solve(assignation));
            }
            default -> { return 0.0; }
        }
    }
}
