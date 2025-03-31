package notepad;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileManager {
    private JTextArea area;

    public FileManager(JTextArea area) {
        this.area = area;
    }

    public void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Only .txt files", "txt"));

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                area.read(br, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile() {
        JFileChooser saveAs = new JFileChooser();
        saveAs.setApproveButtonText("Save");
        int actionDialog = saveAs.showOpenDialog(null);
        if (actionDialog == JFileChooser.APPROVE_OPTION) {
            File fileName = new File(saveAs.getSelectedFile() + ".txt");
            try (BufferedWriter outFile = new BufferedWriter(new FileWriter(fileName))) {
                area.write(outFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}