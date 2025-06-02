package org.opencalc.math;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean W_KEY = false;
    public boolean A_KEY = false;
    public boolean S_KEY = false;
    public boolean D_KEY = false;
    public boolean SPACE_KEY = false;
    public boolean R_KEY = false;
    public boolean PLUS_KEY = false;
    public boolean MINUS_KEY = false;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W -> {W_KEY = true;}
            case KeyEvent.VK_A -> {A_KEY = true;}
            case KeyEvent.VK_S -> {S_KEY = true;}
            case KeyEvent.VK_D -> {D_KEY = true;}
            case KeyEvent.VK_SPACE -> {SPACE_KEY = true;}
            case KeyEvent.VK_R -> {R_KEY = true;}
            case KeyEvent.VK_PLUS -> {PLUS_KEY = true;}
            case KeyEvent.VK_MINUS -> {MINUS_KEY = true;}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W -> {W_KEY = false;}
            case KeyEvent.VK_A -> {A_KEY = false;}
            case KeyEvent.VK_S -> {S_KEY = false;}
            case KeyEvent.VK_D -> {D_KEY = false;}
            case KeyEvent.VK_SPACE -> {SPACE_KEY = false;}
            case KeyEvent.VK_R -> {R_KEY = false;}
            case KeyEvent.VK_PLUS -> {PLUS_KEY = false;}
            case KeyEvent.VK_MINUS -> {MINUS_KEY = false;}
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
