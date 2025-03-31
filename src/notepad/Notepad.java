package notepad;

import memento.Memento;
import memento.Originator;
import memento.Caretaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Notepad extends JFrame implements ActionListener {
    private JTextArea area;
    private FileManager fileManager;
    private TextEditor textEditor;
    private ThemeManager themeManager;
    private MenuHandler menuHandler;

    // Memento pattern components for Undo/Redo
    private Originator originator;
    private Caretaker caretaker;      // Stack for undo
    private Caretaker redoCaretaker;  // Stack for redo
    private boolean isUndoRedoInProgress;

    public Notepad() {
        super("Notepad");
        setSize(1920, 1080);
        setLayout(new BorderLayout());

        // Initialize the text area
        area = new JTextArea();
        area.setFont(new Font("SAN_SERIF", Font.PLAIN, 20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scpane = new JScrollPane(area);

        // Initialize helper classes
        fileManager = new FileManager(area);
        textEditor = new TextEditor(area);
        themeManager = new ThemeManager(area);

        // Initialize the Memento pattern objects
        originator = new Originator();
        caretaker = new Caretaker();
        redoCaretaker = new Caretaker();
        isUndoRedoInProgress = false;

        // Listen to every text change, so we can save states automatically
        area.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUndoRedoInProgress) {
                    saveState();
                    clearRedoStack();
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUndoRedoInProgress) {
                    saveState();
                    clearRedoStack();
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not typically called for plain text, but we'll handle it anyway
                if (!isUndoRedoInProgress) {
                    saveState();
                    clearRedoStack();
                }
            }
        });

        // Capture the initial (empty) state
        originator.setState(area.getText());
        caretaker.addMemento(originator.createMemento());

        // Set up the menu
        menuHandler = new MenuHandler(this);
        setJMenuBar(menuHandler.getMenuBar());

        // Add components
        add(scpane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Save the current text area state to the Undo stack
    private void saveState() {
        originator.setState(area.getText());
        caretaker.addMemento(originator.createMemento());
    }

    // Clear the Redo stack whenever a new edit is made
    private void clearRedoStack() {
        redoCaretaker = new Caretaker();
    }

    // Undo operation
    private void undoAction() {
        isUndoRedoInProgress = true;
        // Push the current state onto the Redo stack
        redoCaretaker.addMemento(originator.createMemento());

        // Pop from the Undo stack
        Memento previousState = caretaker.getMemento();
        if (previousState != null) {
            originator.restoreFromMemento(previousState);
            area.setText(originator.getState());
        } else {
            System.out.println("No more undo steps available.");
        }
        isUndoRedoInProgress = false;
    }

    // Redo operation
    private void redoAction() {
        isUndoRedoInProgress = true;
        // Push the current state onto the Undo stack
        caretaker.addMemento(originator.createMemento());

        // Pop from the Redo stack
        Memento nextState = redoCaretaker.getMemento();
        if (nextState != null) {
            originator.restoreFromMemento(nextState);
            area.setText(originator.getState());
        } else {
            System.out.println("No more redo steps available.");
        }
        isUndoRedoInProgress = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
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
            case "Undo":
                undoAction();
                break;
            case "Redo":
                redoAction();
                break;
            case "New":
                area.setText("");
                // Optionally capture a new "empty" state here if desired
                break;
            case "Print":
                try {
                    area.print();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                // Do nothing
        }
    }

    public static void main(String[] args) {
        new Notepad();
    }
}
