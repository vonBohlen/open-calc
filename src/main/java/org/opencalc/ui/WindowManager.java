package org.opencalc.ui;

import org.opencalc.ui.debug.DebugWindow;
import org.opencalc.ui.gui.GuiWindow;

import javax.swing.*;
import java.awt.*;

public class WindowManager {

    private static void setOpenGL() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) System.setProperty("sun.java2d.opengl", "true");
        System.out.println("[Linux only]" + " OpenGL enabled: " + System.getProperty("sun.java2d.opengl"));
    }

    private static void loadGuiWindow() {
        JFrame root = new JFrame();
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setExtendedState(Frame.MAXIMIZED_BOTH);
        root.setResizable(true);
        root.setTitle("Open calc");
//        JButton button = new JButton("+");
//        button.setBounds(0,root.getHeight()-10,20,10);
//        root.add(button);
        new GuiWindow(root);
        //frame.add(DataHand.renderMan);
        root.pack();
        root.setLocationRelativeTo(null);
        root.setVisible(true);
    }

    private static void loadDebugWindow() {
        JFrame debugWindow = new JFrame();
        debugWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        debugWindow.setSize(1200, 800);
        debugWindow.setResizable(true);
        debugWindow.setTitle("Debug");
        debugWindow.add(new DebugWindow(debugWindow));
        debugWindow.setLocationRelativeTo(null);
        debugWindow.setVisible(true);
    }

    public static void init() {
        setOpenGL();
        loadGuiWindow();

    }

}
