package com.depo.ui;

import javax.swing.SwingUtilities;

public class AppMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
