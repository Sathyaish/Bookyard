package Practice.Java.Swing;

import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

public class DetailsPanelAddButtonListenerArgs extends ActionEvent {

	protected String name;
	protected String occupation;
	protected JTextArea textArea;
	
	public DetailsPanelAddButtonListenerArgs(Object source, 
			String name, String occupation, JTextArea textArea) {
	
		super(source, 0, "Button Clicked");
		
		this.name = name;
		this.occupation = occupation;
		this.textArea = textArea;
	}

	private static final long serialVersionUID = 1L;
	
	public String getName() {
		return this.name;
	}
	
	public String getOccupation() {
		return this.occupation;
	}
	
	public JTextArea getTextArea() { 
		return this.textArea;
	}
}
