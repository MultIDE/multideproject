package com.multideproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class Login {
 public static Point mouseDownScreenCoords;
 public static Point mouseDownCompCoords;
 public JPanel p;
 public JPanel p2;
 public JPanel container;
 public static Color bg = new Color(0x1a1a1a);
 public static Color co = new Color(0x333333);
 public Border unselected = BorderFactory.createLineBorder(co);
 public Border selected = BorderFactory.createLineBorder(new Color(0x68E86D));
 public static Color wrong = new Color(0x7A0417);
 public static Border wrongB = BorderFactory.createLineBorder(wrong);
 static final JTextField user = new JTextField(20);
 static final JPasswordField passw = new JPasswordField(20);
 public static JFrame f;
 public static int accountN;
 public static String[][] account;
 
 public Login() {
	 
	 
  
  user.setBorder(unselected);
  passw.setBorder(unselected);
  user.setBackground(bg);
  passw.setBackground(bg);
  user.setForeground(Color.WHITE);
  passw.setForeground(Color.WHITE);
  p = new JPanel();
  Dimension pd = new Dimension(500, 400);
  p.setPreferredSize(pd);

     passw.addKeyListener(
             new KeyListener(){
                 public void keyPressed(KeyEvent e){
                     if(e.getKeyChar() == KeyEvent.VK_ENTER){
                      format(user.getText(), passw.getPassword());
                     }       
                 }
                 public void keyReleased(KeyEvent arg0) {}public void keyTyped(KeyEvent arg0){}}
         );
     user.addKeyListener(
             new KeyListener(){
                 public void keyPressed(KeyEvent e){
                     if(e.getKeyChar() == KeyEvent.VK_ENTER){
                      format(user.getText(), passw.getPassword());
                     }
                 }
                 public void keyReleased(KeyEvent arg0) {}public void keyTyped(KeyEvent arg0){}}
         );
     passw.addFocusListener(
       new FocusListener() {

     @Override
     public void focusGained(FocusEvent arg0) {
      passw.setBackground(bg);
      passw.setBorder(selected);
      
     }

     @Override
     public void focusLost(FocusEvent arg0) {
      passw.setBorder(unselected);
     }
        
       });
     user.addFocusListener(
       new FocusListener() {

     @Override
     public void focusGained(FocusEvent arg0) {
      user.setBackground(bg);
      user.setBorder(selected);
      
     }

     @Override
     public void focusLost(FocusEvent arg0) {
      user.setBorder(unselected);
     }
        
       });
     
     user.setHorizontalAlignment(JTextField.CENTER);
     passw.setHorizontalAlignment(JPasswordField.CENTER);
     
       GridBagLayout layout = new GridBagLayout();
       
       p.setLayout(layout);        
       GridBagConstraints c = new GridBagConstraints();
        
        c.ipady = 30;
         c.weightx = 0.0;
         c.gridwidth = 3;
         c.gridx = 0;
         c.gridy = 2;
         p.add(user, c);
         
         c.ipady = 35;
         c.weightx = 0.0;
         c.gridwidth = 3;
         c.gridx = 0;
         c.gridy = 4;
         p.add(passw, c);
         
         p2 = new JPanel();
      JButton submit = new JButton("Login");
      submit.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent evt) {
        format(user.getText(), passw.getPassword());
       }
      });
      Dimension ll = new Dimension(75, 40);
      submit.setPreferredSize(ll);
      submit.setRolloverEnabled(true);
      submit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      p2.add(submit);
      Dimension p2d = new Dimension(500, 100);
      p2.setPreferredSize(p2d);
      
      p.setOpaque(false);
      p2.setOpaque(false);
      
      container = new JPanel();
      container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
      container.add(p);
      container.add(p2);
      container.setBackground(co);
         
 }
 
 public void format(String username, char[] pass) {
  String password = new String(pass);
  user.setBackground(bg);
  passw.setBackground(bg);
  if (password.equals("") ||username.equals("") || username.contains(" ")) {
   if (password.equals("")) {
    passw.setText(null);
    passw.setBackground(wrong);
    
   }
   if (username.equals("") || username.contains(" ")) {
    user.setText(null);
    user.setBackground(wrong);
   }
  }
  else {
   validate(username, password);
  }
 }
 
 public static void validate(String u, String p) {
  boolean uGood = false;
  boolean pGood = false;
  String[] usernames = new String[5];
  String[] passwords = new String[usernames.length];
  account = new String[usernames.length][2];
  accountN = -1;
  
  usernames[0] = "Don";
  passwords[0] = "password";
  usernames[1] = "Jared";
  passwords[1] = "password";
  usernames[2] = "Admin";
  passwords[2] = "password";
  usernames[3] = "User4";
  passwords[3] = "password";
  usernames[4] = "User5";
  passwords[4] = "password";
  
  
  for (int i = 0; i < usernames.length; i++) {
   account[i][0] = usernames[i];
   account[i][1] = passwords[i];
   
   if (usernames[i].equals(u) && passwords[i].equals(p)) {
    uGood = true;
    pGood = true;
    accountN = i;
   }
  }
  if (uGood && pGood) {
   f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   f.dispose();
   clearAll();
   new MultIDE(6225);
  }
  else {
   alert("Incorrect Username\nand/or Password.", "Incorrect Input");
   clear(passw);
  }
 }
 
 public static void clearAll() {
  user.setText(null);
  passw.setText(null);
  user.requestFocus();
  
 }
 
 
 public static void clear(JPasswordField p) {
  p.setText(null);
  p.requestFocus();
 }
 public static void alert(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Error: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

 public static void main(String[] args) {
  f = new JFrame();
  f.setUndecorated(true);
  Login login = new Login();
  JPanel upper = new JPanel();
  final JPanel close = new JPanel();
  JPanel spacing = new JPanel();
  upper.setBackground(new Color(0x333333));
  close.setBackground(new Color(0xCC2727));
  close.setBorder(BorderFactory.createLineBorder(Color.red, 1));
  close.setPreferredSize(new Dimension(30, 30));
  close.addMouseListener(new MouseListener() {

   @Override
   public void mouseClicked(MouseEvent e) {
    System.exit(0);
   }
   @Override
   public void mouseEntered(MouseEvent e) {
    close.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
   }
   @Override
   public void mouseExited(MouseEvent e) {
    close.setBorder(BorderFactory.createLineBorder(Color.red, 1));
   }
   @Override
   public void mousePressed(MouseEvent arg0) {
   }
   @Override
   public void mouseReleased(MouseEvent arg0) {
   }
  });
  upper.setPreferredSize(new Dimension(500, 40));
  spacing.setPreferredSize(new Dimension(440, 30));
  spacing.setBackground(new Color(0x333333));
  upper.add(spacing, BorderLayout.WEST);
  upper.add(close, BorderLayout.EAST);
  f.setPreferredSize(new Dimension(500, 550));
  f.setVisible(true);
  f.setResizable(false);
  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  f.setTitle("Login to MultIDE");
     f.add(login.container);
     f.add(upper, BorderLayout.NORTH);
  f.addMouseListener(new MouseListener(){
            public void mousePressed(MouseEvent e) {
              mouseDownScreenCoords = e.getLocationOnScreen();
                 mouseDownCompCoords = e.getPoint();
            }
   @Override
   public void mouseClicked(MouseEvent arg0) {}
   @Override
   public void mouseEntered(MouseEvent arg0) {}
   @Override
   public void mouseExited(MouseEvent arg0) {}
   @Override
   public void mouseReleased(MouseEvent arg0) {}
        });
  f.addMouseMotionListener(new MouseMotionListener(){
            public void mouseMoved(MouseEvent e) {
            }
            public void mouseDragged(MouseEvent e) {
             if (e.getModifiers() == InputEvent.BUTTON1_MASK || e.getModifiers() == InputEvent.BUTTON1_MASK + InputEvent.BUTTON3_MASK) {
              Point currCoords = e.getLocationOnScreen();
              f.setLocation(mouseDownScreenCoords.x + (currCoords.x - mouseDownScreenCoords.x) - mouseDownCompCoords.x,
                     mouseDownScreenCoords.y + (currCoords.y - mouseDownScreenCoords.y) - mouseDownCompCoords.y);
             }
            }
        });
  
  f.setIconImage(new ImageIcon("finished logo small.gif").getImage());
  //Add Icon to JFrame and taskbar Icon
  	ImageIcon icon;
	icon = new ImageIcon("res/MultIDE_Logo_Small.png");
	f.setIconImage(icon.getImage());
  
  f.pack();
  f.setLocationRelativeTo(null);
  
  try {
   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } catch (Exception e) {
   e.printStackTrace();
  }
  
     if (!user.hasFocus()) {
      user.requestFocus();
     }
  
 }

}
