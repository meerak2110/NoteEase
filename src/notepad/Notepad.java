package notepad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Notepad extends JFrame implements ActionListener {
    private JTextArea area;
    private FileManager fileManager;
    private TextEditor textEditor;
    private ThemeManager themeManager;
    private MenuHandler menuHandler;

    public Notepad() {
        super("Notepad");
        setSize(800, 600);
        setLayout(new BorderLayout());

        area = new JTextArea();
        area.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scpane = new JScrollPane(area);

        fileManager = new FileManager(area);
        textEditor = new TextEditor(area);
        themeManager = new ThemeManager(area);
        menuHandler = new MenuHandler(this);

        setJMenuBar(menuHandler.getMenuBar());
        add(scpane, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Open": fileManager.openFile(); break;
            case "Save": fileManager.saveFile(); break;
            case "Copy": textEditor.copy(); break;
            case "Paste": textEditor.paste(); break;
            case "Cut": textEditor.cut(); break;
            case "Select All": textEditor.selectAll(); break;
            case "Toggle Dark Mode": themeManager.toggleTheme(); break;
            case "Exit": System.exit(0); break;
            case "About Notepad": new About().setVisible(true); break;
        }
    }

    public static void main(String[] args) {
        new Notepad();
    }
}