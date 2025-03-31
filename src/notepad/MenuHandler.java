package notepad;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuHandler {
    private JMenuBar menuBar;
    private JMenu file, edit, about;
    private JMenuItem newDoc, open, save, print, exit, copy, paste, cut, selectAll, darkMode, notepad;

    public MenuHandler(ActionListener listener) {  // Pass Notepad as listener
        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        about = new JMenu("Help");

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

        // Add action listeners
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

        file.add(newDoc);
        file.add(open);
        file.add(save);
        file.add(print);
        file.add(exit);

        edit.add(copy);
        edit.add(paste);
        edit.add(cut);
        edit.add(selectAll);
        edit.add(darkMode);

        about.add(notepad);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(about);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}