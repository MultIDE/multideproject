package com.multideproject.menus;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;

import com.multideproject.MultIDE;

public class TextAreaPopupMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	
	private JPopupMenu menu;
	private JMenu submenu;
	private JMenuItem item;
	
	public TextAreaPopupMenu() {
		item = new JMenuItem("Cut");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String toCopy = MultIDE.txtArea.getSelectedText();
			}
		});
		item = new JMenuItem("Copy");
		item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String toCopy = MultIDE.txtArea.getSelectedText();
				StringSelection selectionToCopy = new StringSelection(toCopy);
				MultIDE.clpbrd.setContents(selectionToCopy, selectionToCopy);
			}
		});
		add(item);
		item = new JMenuItem("Paste");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String toPaste = new String();
				 Transferable t = MultIDE.clpbrd.getContents(this);
				 if (t==null) {
					 return;
				 }
				try {
					toPaste = (String)t.getTransferData(DataFlavor.stringFlavor);
					MultIDE.txtArea.getDocument().insertString(MultIDE.txtArea.getCaretPosition(),
							toPaste, MultIDE.ATTR_NONE);
				} catch (BadLocationException | UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		add(item);
		item = new JMenuItem("Send");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String toSend = new String();
				toSend = MultIDE.txtArea.getSelectedText();
				MultIDE.send(toSend);
			}
		});
		add(item);
	}
	
	
}
