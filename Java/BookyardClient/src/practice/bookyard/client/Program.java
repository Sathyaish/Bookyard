package practice.bookyard.client;

import javax.swing.SwingUtilities;

public class Program {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new LoginDialogEventLoop());
		
	}

}