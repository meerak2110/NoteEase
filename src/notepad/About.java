/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class About extends JFrame implements ActionListener {
    JButton b1, darkModeButton;
    JLabel l3; // Make l3 accessible

    About() {
        getContentPane().setBackground(Color.WHITE); // Set default background
        setBounds(600, 200, 700,600);
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("notepad/icons/windows.png"));
        Image i2 = i1.getImage().getScaledInstance(400, 80, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);
        l1.setBounds(150, 40, 400, 80);
        add(l1);
        
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("notepad/icons/notepad.png"));
        Image i5 = i4.getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l2 = new JLabel(i6);
        l2.setBounds(50, 180, 70, 70);
        add(l2);
        
        l3 = new JLabel("<html>@Author Tejas Rajendra Donadkar<br>IIIT NAGPUR<br>2023 MINOR PROJECT. All rights reserved<br><br>Notepad is a word processing program, <br>which allows changing of text in a computer file.<br>Notepad is a simple text editor for basic text-editing program<br> which enables computer users to create documents. </html>");
        l3.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        l3.setBounds(150, 130, 500, 300);
        add(l3);
        
        b1 = new JButton("OK");
        b1.setBounds(580, 500, 80, 25);
        b1.addActionListener(this);
        add(b1);
        
        // Add Dark Mode Button
        darkModeButton = new JButton("Dark Mode");
        darkModeButton.setBounds(480, 500, 100, 25);
        darkModeButton.addActionListener(this);
        add(darkModeButton);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            this.setVisible(false);
        } else if (ae.getSource() == darkModeButton) {
            boolean isDarkMode = getContentPane().getBackground().equals(Color.DARK_GRAY);
            if (isDarkMode) {
                getContentPane().setBackground(Color.WHITE);
                l3.setForeground(Color.BLACK);
                darkModeButton.setText("Dark Mode");
            } else {
                getContentPane().setBackground(Color.DARK_GRAY);
                l3.setForeground(Color.WHITE);
                darkModeButton.setText("Light Mode");
            }
        }
    }
    
    public static void main(String[] args) {
        new About().setVisible(true);
    }
}
