package practice.bookyard.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

public class LoginDialogEventLoop implements Runnable {

	@Override
	public void run() {
		
		// Create and set title
		JDialog dialog = new LoginDialog("Bookyard Login");
		
		// Set default close behavior
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		// Set size
		dialog.setSize(400, 300);
		// dialog.setResizable(false);
		
		// Set position on the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((int)screenSize.getWidth() / 2, (int)screenSize.getHeight() / 2);
		dialog.setLocationRelativeTo(null);
		
		dialog.setVisible(true);
	}

}
