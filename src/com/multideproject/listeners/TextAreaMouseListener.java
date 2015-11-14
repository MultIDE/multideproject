package com.multideproject.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.multideproject.menus.TextAreaPopupMenu;

public class TextAreaMouseListener implements MouseListener {
	
	private TextAreaPopupMenu tapm;
	
	
	public TextAreaMouseListener(TextAreaPopupMenu tapm) {
		this.tapm = tapm;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			doPop(e, e.getX(), e.getY());
	}
	
	public void doPop(MouseEvent e, int x, int y) {
		tapm.show(e.getComponent(), x, y);
	}

}
