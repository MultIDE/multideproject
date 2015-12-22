package com.multideproject.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class LinePainter implements Highlighter.HighlightPainter, CaretListener, MouseListener, MouseMotionListener {
	private JTextComponent component;
	private Color color
	private Rectangle lastView;
	public LinePainter(JTextComponent component)
	{
		this(component, null);
		setLighter(component.getSelectionColor());
	}
	public LinePainter(JTextComponent component, Color color)
	{
		this.component = component;
		setColor( color );
		component.addCaretListener( this );
		component.addMouseListener( this );
		component.addMouseMotionListener( this );
		try
		{
			component.getHighlighter().addHighlight(0, 0, this);
		}
		catch(BadLocationException ble) {}
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setLighter(Color color)
	{
		int red   = Math.min(255, (int)(color.getRed() * 1.2));
		int green = Math.min(255, (int)(color.getGreen() * 1.2));
		int blue  = Math.min(255, (int)(color.getBlue() * 1.2));
		setColor(new Color(red, green, blue));
	}

	public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c)
	{
		try
		{
			Rectangle r = c.modelToView(c.getCaretPosition());
			g.setColor( color );
			g.fillRect(0, r.y, c.getWidth(), r.height);

			if (lastView == null)
				lastView = r;
		}
		catch(BadLocationException ble) {System.out.println(ble);}
	}
	
	private void resetHighlight()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					int offset =  component.getCaretPosition();
					Rectangle currentView = component.modelToView(offset);
					if (lastView.y != currentView.y)
					{
						component.repaint(0, lastView.y, component.getWidth(), lastView.height);
						lastView = currentView;
					}
				}
				catch(BadLocationException ble) {}
			}
		});
	}

	public void caretUpdate(CaretEvent e)
	{
		resetHighlight();
	}

	public void mousePressed(MouseEvent e)
	{
		resetHighlight();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void mouseDragged(MouseEvent e)
	{
		resetHighlight();
	}

	public void mouseMoved(MouseEvent e) {}
}
