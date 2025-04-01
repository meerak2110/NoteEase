package notepad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import memento.*;
import java.util.HashMap;
import java.util.Map;

public class Notepad extends JFrame implements ActionListener {

    private JTabbedPane tabbedPane;
    private FileManager fileManager;
    private TextEditor textEditor;
    private ThemeManager themeManager;
    private MenuHandler menuHandler;
    private JPanel statusPanel;
    private JLabel wordCountLabel;
    private WordCounter wordCounter;

    // Maps to store tab-specific memento objects
    private Map<Integer, Originator> originatorMap;
    private Map<Integer, Caretaker> undoCaretakerMap;
    private Map<Integer, Caretaker> redoCaretakerMap;
    private Map<Integer, Boolean> isUndoRedoInProgressMap;

    public Notepad() {
        super("Notepad");
        setSize(1920, 1080);
        setLayout(new BorderLayout());

        // Initialize the tabbed pane to handle multiple tabs
        tabbedPane = new JTabbedPane();
        
        // Add a change listener to update the current text area when switching tabs
        tabbedPane.addChangeListener(e -> {
            updateTextEditor();
            updateWordCount();
        });

        statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wordCountLabel = new JLabel("Words: 0");
        statusPanel.add(wordCountLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Initialize maps for tab-specific memento objects
        originatorMap = new HashMap<>();
        undoCaretakerMap = new HashMap<>();
        redoCaretakerMap = new HashMap<>();
        isUndoRedoInProgressMap = new HashMap<>();

        // Initialize helper classes
        fileManager = new FileManager(null);
        textEditor = new TextEditor(null);
        themeManager = new ThemeManager(null); // Initialize with null, we'll set it dynamically

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

        // Initialize WordCounter for this tab
        wordCounter = new WordCounter(area, wordCountLabel);

        JScrollPane scpane = new JScrollPane(area);
        int newIndex = tabbedPane.getTabCount();
        tabbedPane.addTab("New Document", scpane);

        // Initialize memento objects for this tab
        originatorMap.put(newIndex, new Originator());
        undoCaretakerMap.put(newIndex, new Caretaker());
        redoCaretakerMap.put(newIndex, new Caretaker());
        isUndoRedoInProgressMap.put(newIndex, false);

        // Add document listener for undo/redo functionality
        area.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                int currentIndex = tabbedPane.indexOfComponent(scpane);
                if (!isUndoRedoOperation(currentIndex)) {
                    saveState(currentIndex, area);
                    updateWordCount();
                }
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                int currentIndex = tabbedPane.indexOfComponent(scpane);
                if (!isUndoRedoOperation(currentIndex)) {
                    saveState(currentIndex, area);
                    updateWordCount();
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                // Plain text components don't fire these events
            }
        });

        // Add close button to the tab
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("New Document");
        JButton closeButton = new JButton("X");

        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfTabComponent(panel);
            closeTab(index);
        });

        panel.add(label, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.EAST);

        tabbedPane.setTabComponentAt(newIndex, panel);
        tabbedPane.setSelectedIndex(newIndex);

        // Save initial state
        saveState(newIndex, area);
        
        // Update text editor with the current text area
        updateTextEditor();
    }

    // Check if undo/redo operation is in progress to prevent recursive document updates
    private boolean isUndoRedoOperation(int tabIndex) {
        Boolean inProgress = isUndoRedoInProgressMap.get(tabIndex);
        return inProgress != null && inProgress;
    }

    // Set undo/redo operation status
    private void setUndoRedoInProgress(int tabIndex, boolean status) {
        isUndoRedoInProgressMap.put(tabIndex, status);
    }

    // Update the text editor with the current text area
    private void updateTextEditor() {
        JTextArea currentArea = getCurrentTextArea();
        if (currentArea != null) {
            textEditor = new TextEditor(currentArea);
            themeManager = new ThemeManager(currentArea);
        }
    }
    
    // Update the word count in the status bar
    private void updateWordCount() {
        JTextArea currentArea = getCurrentTextArea();
        if (currentArea != null) {
            wordCounter = new WordCounter(currentArea, wordCountLabel);
            wordCounter.updateWordCount();
        }
    }

    // Save the current state for undo/redo
    private void saveState(int tabIndex, JTextArea area) {
        if (tabIndex < 0) return;
        
        Originator originator = originatorMap.get(tabIndex);
        Caretaker undoCaretaker = undoCaretakerMap.get(tabIndex);
        
        if (originator != null && undoCaretaker != null) {
            originator.setState(area.getText());
            undoCaretaker.addMemento(originator.createMemento());
            // Clear redo stack when new changes are made
            redoCaretakerMap.put(tabIndex, new Caretaker());
        }
    }

    // Perform undo operation
    public void undo() {
        int currentIndex = tabbedPane.getSelectedIndex();
        if (currentIndex < 0) return;
        
        JTextArea currentArea = getCurrentTextArea();
        Originator originator = originatorMap.get(currentIndex);
        Caretaker undoCaretaker = undoCaretakerMap.get(currentIndex);
        Caretaker redoCaretaker = redoCaretakerMap.get(currentIndex);
        
        if (currentArea != null && originator != null && 
            undoCaretaker != null && redoCaretaker != null && 
            undoCaretaker.getStack().size() > 1) {
            
            try {
                // Mark as undo/redo in progress to prevent document listener from interfering
                setUndoRedoInProgress(currentIndex, true);
                
                // Save current state to redo stack first
                originator.setState(currentArea.getText());
                redoCaretaker.addMemento(originator.createMemento());

                // Remove current state from undo stack
                Memento currentState = undoCaretaker.getMemento();
                
                // Get previous state (if any)
                if (!undoCaretaker.getStack().isEmpty()) {
                    Memento previousState = undoCaretaker.getStack().peek();
                    originator.restoreFromMemento(previousState);
                    currentArea.setText(originator.getState());
                }
            } finally {
                // Always reset the flag when done
                setUndoRedoInProgress(currentIndex, false);
            }
        }
    }

    // Perform redo operation
    public void redo() {
        int currentIndex = tabbedPane.getSelectedIndex();
        if (currentIndex < 0) return;
        
        JTextArea currentArea = getCurrentTextArea();
        Originator originator = originatorMap.get(currentIndex);
        Caretaker undoCaretaker = undoCaretakerMap.get(currentIndex);
        Caretaker redoCaretaker = redoCaretakerMap.get(currentIndex);
        
        if (currentArea != null && originator != null && 
            undoCaretaker != null && redoCaretaker != null && 
            !redoCaretaker.getStack().isEmpty()) {
            
            try {
                // Mark as undo/redo in progress to prevent document listener from interfering
                setUndoRedoInProgress(currentIndex, true);
                
                // Save current state to undo stack first
                originator.setState(currentArea.getText());
                undoCaretaker.addMemento(originator.createMemento());

                // Get state from redo stack
                Memento nextState = redoCaretaker.getMemento();
                if (nextState != null) {
                    originator.restoreFromMemento(nextState);
                    currentArea.setText(originator.getState());
                }
            } finally {
                // Always reset the flag when done
                setUndoRedoInProgress(currentIndex, false);
            }
        }
    }

    // Close the specified tab
    private void closeTab(int index) {
        if (index < 0) return;
        
        if (tabbedPane.getTabCount() > 1) {
            // Remove tab and memento objects for this tab
            tabbedPane.remove(index);
            originatorMap.remove(index);
            undoCaretakerMap.remove(index);
            redoCaretakerMap.remove(index);
            isUndoRedoInProgressMap.remove(index);
            
            // Update maps to reflect new tab indices
            Map<Integer, Originator> newOriginatorMap = new HashMap<>();
            Map<Integer, Caretaker> newUndoMap = new HashMap<>();
            Map<Integer, Caretaker> newRedoMap = new HashMap<>();
            Map<Integer, Boolean> newInProgressMap = new HashMap<>();
            
            for (int i = 0; i < index; i++) {
                // Keep tabs before the removed one
                newOriginatorMap.put(i, originatorMap.get(i));
                newUndoMap.put(i, undoCaretakerMap.get(i));
                newRedoMap.put(i, redoCaretakerMap.get(i));
                newInProgressMap.put(i, isUndoRedoInProgressMap.get(i));
            }
            
            for (int i = index; i < tabbedPane.getTabCount(); i++) {
                // Shift tabs after the removed one
                newOriginatorMap.put(i, originatorMap.get(i+1));
                newUndoMap.put(i, undoCaretakerMap.get(i+1));
                newRedoMap.put(i, redoCaretakerMap.get(i+1));
                newInProgressMap.put(i, isUndoRedoInProgressMap.get(i+1));
            }
            
            originatorMap = newOriginatorMap;
            undoCaretakerMap = newUndoMap;
            redoCaretakerMap = newRedoMap;
            isUndoRedoInProgressMap = newInProgressMap;
            
        } else {
            JOptionPane.showMessageDialog(this, "You cannot close the last tab.", "Cannot Close",
                    JOptionPane.WARNING_MESSAGE);
        }
        
        // Update text editor with the current text area
        updateTextEditor();
        updateWordCount();
    }

    // Get the currently selected JTextArea
    public JTextArea getCurrentTextArea() {
        JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
        if (scrollPane != null) {
            return (JTextArea) scrollPane.getViewport().getView();
        }
        return null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "New Tab":
                createNewTab(); // Create a new tab when "New Tab" is clicked
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
            case "Undo":
                undo();
                break;
            case "Redo":
                redo();
                break;
            case "Toggle Dark Mode":
                JTextArea currentTextArea = getCurrentTextArea();
                if (currentTextArea != null) {
                    themeManager = new ThemeManager(currentTextArea); // Apply theme dynamically
                    themeManager.toggleTheme();
                }
                break;
            case "Word Count":
                JTextArea currentArea = getCurrentTextArea();
                if (currentArea != null) {
                    new WordCounter(currentArea, wordCountLabel).showWordCountDialog(this);
                }
                break;
            case "Exit":
                System.exit(0);
                break;
            case "About Notepad":
                new About().setVisible(true);
                break;
            default:
            // Handle other actions
        }
    }

    public static void main(String[] args) {
        new Notepad();
    }
}