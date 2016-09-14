package practice.bookyard.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class LoginDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	protected JLabel lblTopSpace = null;
	protected JPanel loginPanel = null;
	protected JPanel statusPanel = null;
	protected JLabel lblStatus = null;
	
	public LoginDialog(String title) {
		
		super(null, title, ModalityType.APPLICATION_MODAL);
		
		this.setTitle(title);
		
		Initialize();
	}
	
	protected void Initialize() {
		
		lblTopSpace = new JLabel("Login into Bookyard");
		lblTopSpace.setForeground(this.getBackground());
		
		loginPanel = new LoginPanel(this);
		statusPanel = new JPanel();
		
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setSize(this.getWidth(), 50);
		statusPanel.setLayout(new BorderLayout());
		
		lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lblStatus, BorderLayout.SOUTH);
		
		this.setLayout(new BorderLayout());
		Container container = this.getContentPane();
		
		container.add(lblTopSpace, BorderLayout.NORTH);
		container.add(loginPanel, BorderLayout.CENTER);
		container.add(statusPanel, BorderLayout.SOUTH);
		
		this.pack();
	}
	
	public void setStatusLabel(String text, Color color) {
		lblStatus.setForeground(color);
		this.lblStatus.setText(text);
		this.lblStatus.setToolTipText(text);
	}
}
