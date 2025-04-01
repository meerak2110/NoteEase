package notepad;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class WordCounter {
    private JLabel wordCountLabel;
    private JTextComponent textComponent;

    public WordCounter(JTextComponent textComponent, JLabel statusLabel) {
        this.textComponent = textComponent;
        this.wordCountLabel = statusLabel;
        setupDocumentListener();
    }

    private void setupDocumentListener() {
        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateWordCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateWordCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateWordCount();
            }
        });
    }

    public int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        String[] words = text.split("\\s+");
        return words.length;
    }

    public void updateWordCount() {
        int count = countWords(textComponent.getText());
        wordCountLabel.setText("Words: " + count);
    }

    public void showWordCountDialog(Component parent) {
        int count = countWords(textComponent.getText());
        JOptionPane.showMessageDialog(parent, 
            "Word count: " + count, 
            "Word Count", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}