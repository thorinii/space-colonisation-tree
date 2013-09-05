package me.lachlanap.spacecolonisationtree;

import java.io.IOException;
import javax.swing.UIManager;
import me.lachlanap.spacecolonisationtree.gui.Frame;

/**
 * @author Lachlan Phillips
 */
public class Main {

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        Frame frame = new Frame();
        frame.setVisible(true);
    }
}
