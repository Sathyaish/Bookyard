package Practice.Java.Swing;

import javax.swing.JFrame;

public class MainWindowEventLoop implements Runnable {

	@Override
	public void run() {
		JFrame mainWindow = new MainWindow("Main Window");
		mainWindow.setSize(500,  300);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		System.out.println(String.format("Main window loop running on thread %1$d.", Thread.currentThread().getId()));
	}
}
