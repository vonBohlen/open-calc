package org.opencalc.ui.gui;

import javax.swing.*;
import java.util.HashMap;

public class GuiWindow {

    JFrame root;

    HashMap<String, JComponent> components = new HashMap<>();

    public GuiWindow(JFrame root) {
        this.root = root;
        GuiComponents.loadGuiComponents(root, components);
    }

}
