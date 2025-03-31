package notepad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Notepad extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private FileManager fileManager;
    private TextEditor textEditor;
    private ThemeManager themeManager;
    private MenuHandler menuHandler;

    public Notepad() {
        super("Notepad");
        setSize(1920, 1080);
        setLayout(new BorderLayout());

        // Initialize the tabbed pane to handle multiple tabs
        tabbedPane = new JTabbedPane();

        // Initialize helper classes
        fileManager = new FileManager(null);
        textEditor = new TextEditor(null);
        themeManager = new ThemeManager(null);

        // Set up the menu handler
        menuHandler = new MenuHandler(this);
        setJMenuBar(menuHandler.getMenuBar());

        // Add the tabbedPane to the frame
        add(tabbedPane, BorderLayout.CENTER);

        // Create the first tab (default "New Document")
        createNewTab();

        // Show the window
        setVisible(true);
    }

    // Create a new tab with an editable JTextArea
    public void createNewTab() {
        JTextArea area = new JTextArea();
        area.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scpane = new JScrollPane(area);
        int newIndex = tabbedPane.getTabCount();
        tabbedPane.addTab("New Document", scpane);

        // Add a close button to the tab
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("New Document");
        JButton closeButton = new JButton("X");

        closeButton.addActionListener(e -> closeTab(newIndex));

        panel.add(label, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.EAST);

        tabbedPane.setTabComponentAt(newIndex, panel);
    }

    // Close the currently selected tab
    private void closeTab(int index) {
        if (tabbedPane.getTabCount() > 1) {
            tabbedPane.remove(index);
        } else {
            JOptionPane.showMessageDialog(this, "You cannot close the last tab.", "Cannot Close", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "New Tab":
                createNewTab();  // Create a new tab when "New Tab" is clicked
                break;
            case "Open":
                fileManager.openFile();
                break;
            case "Save":
                fileManager.saveFile();
                break;
            case "Copy":
                textEditor.copy();
                break;
            case "Paste":
                textEditor.paste();
                break;
            case "Cut":
                textEditor.cut();
                break;
            case "Select All":
                textEditor.selectAll();
                break;
            case "Toggle Dark Mode":
                themeManager.toggleTheme();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "About Notepad":
                new About().setVisible(true);
                break;
            default:
                // Handle other actions (Undo, Redo, etc.)
        }
    }

    public static void main(String[] args) {
        new Notepad();
    }
}
