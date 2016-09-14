package practice.bookyard.client;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.*;

public class HyperLinkLabel extends JPanel
{
  
  private static final long serialVersionUID = 1L;
  
  private String url = null;
  private String text = null;
  private JLabel label;

  public HyperLinkLabel(String url, String text) {
	  
	  GridLayout gridLayout = new GridLayout(0, 1);
	  gridLayout.setVgap(7);
	  this.setLayout(gridLayout);
	  
	  this.url = url;
	  this.text = text;
	  label = new JLabel();
	  
	  this.setLabel(this.url, this.text);
	  this.add(this.label);
	  
	  Font font = this.label.getFont();
	  Map attributes = font.getAttributes();
	  attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	  this.label.setFont(font.deriveFont(attributes));
	  
	  this.label.addMouseListener(new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			label.setCursor(Cursor.getDefaultCursor());
		}
	  });
  }
  
  public void setLabel(String url, String title) {
	  
	  String text = "<html><a href=\" " + url + "\">" + title + "</a></html>";
	  this.label.setText(text);
  }
  
}