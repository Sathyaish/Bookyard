package Practice.Java.Swing;

import javax.swing.JFrame;

public class SecondWindowEventLoop implements Runnable {

	@Override
	public void run() {
		SecondWindow secondWindow = new SecondWindow("Second Window");
		secondWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		secondWindow.setSize(200, 300);
		secondWindow.setVisible(true);
		System.out.println(String.format("Second window loop running on thread %1$d.", Thread.currentThread().getId()));
	}

}
