/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multideproject.menus;

import com.multideproject.MultIDE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Jared Massa
 */
public class ToolBar extends JMenuBar {
	
	private static final long serialVersionUID = 1L;
	private JMenu menu, submenu;
    private JMenuItem item;
    private JFileChooser fc;
    
    public ToolBar() {
        menu = new JMenu("File");
        item = new JMenuItem("Open File");
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int returnValue = fc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    try {
						MultIDE.LoadFile(selectedFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                }
            }
        });
        menu.add(item);
        item = new JMenuItem("Item one");
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(item);
        add(menu);
        menu = new JMenu("Edit");
        submenu = new JMenu("Tshe;rl");
        menu.add(submenu);
        item = new JMenuItem("asdf asdf");
        menu.add(item);
        add(menu);
    }
    
}
