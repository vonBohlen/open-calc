package org.opencalc.ui;

import org.opencalc.ui.debug.DebugWindow;

import javax.swing.*;
import java.awt.*;

public class WindowManager {

    private static void setOpenGL() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) System.setProperty("sun.java2d.opengl", "true");
        System.out.println("[Linux only]" + " OpenGL enabled: " + System.getProperty("sun.java2d.opengl"));
    }

    private static void loadGuiWindow() {
        JFrame guiWindow = new JFrame();
        guiWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
        guiWindow.setResizable(true);
        guiWindow.setTitle("Open calc");
        //guiWindow.add(DataHand.renderMan);
        guiWindow.pack();
        guiWindow.setLocationRelativeTo(null);
        guiWindow.setVisible(true);
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
        loadDebugWindow();

    }

}
