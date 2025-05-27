package org.opencalc.ui.debug;

import javax.swing.*;
import java.awt.*;

public class DebugWindow extends JPanel {

    public DebugWindow(Frame window) {
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        JScrollPane scrollPane = new JScrollPane(this);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        window.add(scrollPane);
//        setLayout(new GridBagLayout());
//
//        add(new JLabel("Connectors example. You can drag the connected component to see how the line will be changed"),
//                new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    }

}
