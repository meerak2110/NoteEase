package notepad;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private JTextArea area;
    private boolean isDarkMode = false;

    public ThemeManager(JTextArea area) {
        this.area = area;
    }

    public void setTextArea(JTextArea area) {
        this.area = area;
    }

    public void toggleTheme() {
        if (area == null)
            return;

        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            area.setBackground(Color.BLACK);
            area.setForeground(Color.WHITE);
            area.setCaretColor(Color.WHITE);
        } else {
            area.setBackground(Color.WHITE);
            area.setForeground(Color.BLACK);
            area.setCaretColor(Color.BLACK);
        }
    }
}
