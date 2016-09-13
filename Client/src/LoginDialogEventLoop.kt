package bookyard.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

public class LoginDialogEventLoop : Runnable {

    override public fun run() {

        // Create and set title
        val dialog : JDialog = LoginDialog("Bookyard Login");

        // Set default close behavior
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Set size
        dialog.setSize(400, 300);
        // dialog.setResizable(false);

        // Set position on the screen
        val screenSize : Dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(screenSize.getWidth().toInt() / 2, screenSize.getHeight().toInt() / 2);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);
    }
}