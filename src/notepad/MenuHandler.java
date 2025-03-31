package notepad;

import javax.swing.*;
import java.awt.event.*;

public class MenuHandler {
    private JMenuBar menuBar;
    private JMenu file, edit, about;
    private JMenuItem newDoc, open, save, print, exit;
    private JMenuItem copy, paste, cut, selectAll, darkMode, notepad;
    
    // NEW: Undo/Redo menu items
    private JMenuItem undo, redo;

    public MenuHandler(ActionListener listener) {
        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        about = new JMenu("Help");

        // Existing menu items
        newDoc = new JMenuItem("New");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        print = new JMenuItem("Print");
        exit = new JMenuItem("Exit");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        cut = new JMenuItem("Cut");
        selectAll = new JMenuItem("Select All");
        darkMode = new JMenuItem("Toggle Dark Mode");
        notepad = new JMenuItem("About Notepad");

        // NEW: Undo and Redo
        undo = new JMenuItem("Undo");
        // Ctrl+Z for Undo
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undo.addActionListener(listener);

        redo = new JMenuItem("Redo");
        // Ctrl+Y for Redo
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redo.addActionListener(listener);

        // Add action listeners to existing items
        newDoc.addActionListener(listener);
        open.addActionListener(listener);
        save.addActionListener(listener);
        print.addActionListener(listener);
        exit.addActionListener(listener);
        copy.addActionListener(listener);
        paste.addActionListener(listener);
        cut.addActionListener(listener);
        selectAll.addActionListener(listener);
        darkMode.addActionListener(listener);
        notepad.addActionListener(listener);

        // Build the File menu
        file.add(newDoc);
        file.add(open);
        file.add(save);
        file.add(print);
        file.add(exit);

        // Build the Edit menu (add Undo & Redo here)
        edit.add(undo);
        edit.add(redo);
        edit.add(copy);
        edit.add(paste);
        edit.add(cut);
        edit.add(selectAll);
        edit.add(darkMode);

        // Build the Help menu
        about.add(notepad);

        // Add menus to the menu bar
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(about);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
