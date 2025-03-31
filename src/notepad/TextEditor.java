package notepad;

import javax.swing.*;

public class TextEditor {
    private JTextArea area;
    private String clipboard = "";

    public TextEditor(JTextArea area) {
        this.area = area;
    }

    public void copy() {
        clipboard = area.getSelectedText();
    }

    public void paste() {
        area.insert(clipboard, area.getCaretPosition());
    }

    public void cut() {
        clipboard = area.getSelectedText();
        area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd());
    }

    public void selectAll() {
        area.selectAll();
    }
}