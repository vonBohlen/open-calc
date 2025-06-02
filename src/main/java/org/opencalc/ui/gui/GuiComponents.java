package org.opencalc.ui.gui;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GuiComponents {

    private static String[][] key1 = new String[6][6];

    private static void loadKeypad(HashMap<String, JComponent> components) {
        Dimension dss = Toolkit.getDefaultToolkit().getScreenSize();
        int keyHeight = dss.height / 100;
        int keyWidth = dss.width / 100;
        int currentX = 0;
        int currentY = dss.height;
        for (int i = 0; i <= 5; i++) {
            JButton button = new JButton(key1[0][i]);
            button.setBounds(currentX, currentY, keyWidth, keyHeight);
            components.put("key1", button);
            currentX += keyWidth;
            //currentY -= keyHeight;
        }

    }

    public static void loadGuiComponents(JFrame root, HashMap<String, JComponent> components) {
        key1[0][0] = "+";
        key1[0][1] = "-";
        key1[0][2] = "*";
        key1[0][3] = "/";
        key1[0][4] = "^";
        key1[0][5] = "%";
        loadKeypad(components);
        components.forEach((k,v) -> {
            root.add(v);
        });
    }

    public static void displayDefaultComponents() {

    }

}
