/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multideproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.multideproject.GUI.LinePainter;
import com.multideproject.GUI.TextLineNumber;
import com.multideproject.listeners.TextAreaMouseListener;
import com.multideproject.menus.TextAreaPopupMenu;
import com.multideproject.menus.ToolBar;

/**
 * @author Jared Massa
 */
public class MultIDE {
    
	//BEGIN DOCUMENT HANDLING
    final static StyleContext CONT = StyleContext.getDefaultStyleContext();
    //private|public|protected|static|final|new|import
    final static AttributeSet ATTR_VISIBILITY = CONT.addAttribute(CONT.getEmptySet(), StyleConstants.Foreground, new Color(0xDEB150));
    //int|boolean|double|float|short|byte|long|void
    final static AttributeSet ATTR_PRIMITIVE_TYPE = CONT.addAttribute(CONT.getEmptySet(), StyleConstants.Foreground, new Color(0xCB96FF));
    //while|if|else|return|super
    final static AttributeSet ATTR_KEYWORD = CONT.addAttribute(CONT.getEmptySet(), StyleConstants.Foreground, new Color(0x00BF06));
    //Default
    public final static AttributeSet ATTR_NONE = CONT.addAttribute(CONT.getEmptySet(), StyleConstants.Foreground, new Color(0xC7C7C7));
    final static AttributeSet[] ATTRIBUTES = {ATTR_VISIBILITY, ATTR_PRIMITIVE_TYPE, ATTR_KEYWORD};
    private static DefaultStyledDocument document = new DefaultStyledDocument() {
    	
		private static final long serialVersionUID = 1L;
		
		public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offset, str, a);

            String text = getText(0, getLength());
            int before = findLastNonWordChar(text, offset);
            if (before < 0) before = 0;
            int after = findFirstNonWordChar(text, offset + str.length());
            int wordL = before;
            int wordR = before;

            while (wordR <= after) {
                if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    if (text.substring(wordL, wordR).matches("(\\W)*(private|public|protected|synchronized|static|final|new|import)"))
                        setCharacterAttributes(wordL, wordR - wordL, ATTR_VISIBILITY, false);
                    else if (text.substring(wordL, wordR).matches("(\\W)*(int|boolean|double|float|short|byte|long|void|return|package)"))
                    	setCharacterAttributes(wordL, wordR - wordL, ATTR_PRIMITIVE_TYPE, false);
                    else if (text.substring(wordL,  wordR).matches("(\\W)*(while|if|else|super)"))
                    	setCharacterAttributes(wordL, wordR - wordL, ATTR_KEYWORD, false);
                    else
                        setCharacterAttributes(wordL, wordR - wordL, ATTR_NONE, false);
                    wordL = wordR;
                }
                wordR++;
            }
        }
        public void remove (int offs, int len) throws BadLocationException {
            super.remove(offs, len);

            String text = getText(0, getLength());
            int before = findLastNonWordChar(text, offs);
            if (before < 0) before = 0;
            int after = findFirstNonWordChar(text, offs);

            if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
                setCharacterAttributes(before, after - before, ATTR_VISIBILITY, false);
            } else {
                setCharacterAttributes(before, after - before, ATTR_NONE, false);
            }
        }
    };
    private static int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }
    private static int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
    
    //END DOCUMENT OBJECT CREATION AND SETTING
    //BEGIN STANDARD VARIABLE DECLARATION
    public static final int WIDTH = 1000, HEIGHT = 600;
    public static JFrame f;
    public static ImageIcon icon;
    
    private static String ip = "localhost";
    private static int port = 6225;
    private static Socket socket = null;
    private static DataTransferHandler dth;
    
    public static JTextPane txtArea;
    public static JScrollPane txtPane;
    public static TextLineNumber txtNumInterface;
    public static Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
    
    public static TextAreaPopupMenu tapm;
    public static TextAreaMouseListener taml;
    
    public static void main(String[] args) {
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
    	new MultIDE(6225);
    }
    
    public MultIDE(int port) {
    	MultIDE.setPort(port);
    	loadWindow();
    }
    
    public static void send(String s) {
			dth.send(s);
    }
    
    
    public static void establishConnection(String ip, int port) {
           try { 
               socket = new Socket(ip, port);
               dth = new DataTransferHandler(socket);
            } catch (Exception e) {
            e.printStackTrace();
            Object[] options = {"Use in Offline Mode",
                        "Retry Connection",
                        "Exit MultIDE"};
            int n = JOptionPane.showOptionDialog(f,
                "Connection Failed, what do?",
                "D'uh oh.",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);
            if (n == JOptionPane.YES_OPTION) {
            }
            else if (n == JOptionPane.NO_OPTION) {
                establishConnection(ip, port);
            }
            else {
               System.exit(0);
            }

        }
    }
    
    public static void loadWindow() {
            f = new JFrame("MultIDE");
            f.setVisible(true);
            f.setResizable(true);
            f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            f.add(new ToolBar(), BorderLayout.NORTH);
            
            //TextPane (txtArea) and ScrollPane (txtPane) stuff 
            txtArea = new JTextPane(document);
            tapm = new TextAreaPopupMenu();
            taml = new TextAreaMouseListener(tapm);
            txtArea.addMouseListener(taml);
            txtArea.setSelectionColor(new Color(0xFF0000));
            txtArea.setSelectedTextColor(new Color(0xdedede));
            formatTextComponent(txtArea, new Color(0x404040));
            formatStyle(txtArea);
            txtPane = new JScrollPane(txtArea);
            txtNumInterface = new TextLineNumber(txtArea, txtArea.getFont(), new Color(0xF2DA24), new Color(0xA67100), new Color(0x292929));
            txtPane.setRowHeaderView(txtNumInterface);
            txtPane.getVerticalScrollBar().setUnitIncrement(20);
            txtPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            txtPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            f.add(txtPane);
            
            //Add Icon to JFrame and taskbar Icon
        	icon = new ImageIcon("res/MultIDE_Logo_Small.png");
    		f.setIconImage(icon.getImage());
            
            f.pack();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLocationRelativeTo(null);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
                establishConnection(ip, port);
            txtArea.requestFocusInWindow();
    }
    
    public static void formatTextComponent(JTextComponent component, Color lighterColor) {
        LinePainter lp = new LinePainter(component);
        lp.setLighter(lighterColor);
        component.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        component.setFont(new Font("Consolas", Font.PLAIN, 14));
    }
    
    public static void formatStyle(JTextPane pane) {
    	pane.setBackground(new Color(0x333333));
    	pane.setCaretColor(new Color(0xC7C7C7));
    }
    
    @SuppressWarnings("resource")
	public static void LoadFile(File file) throws IOException {
        txtArea.setText("");
        FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        
        String totalText = "";
        for (int i = 0; i < buffer.limit(); i++)
        {
            totalText += Character.toString(((char) buffer.get()));
        }
        txtArea.setText(totalText); 
        f.setTitle("MultIDE - " + file.getPath());
    }
    
	public static int getPort() {
		return port;
	}
	public static void setPort(int port) {
		MultIDE.port = port;
	}
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		MultIDE.ip = ip;
	}
    
}
