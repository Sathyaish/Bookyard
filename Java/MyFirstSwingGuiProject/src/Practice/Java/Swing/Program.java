package Practice.Java.Swing;

import javax.swing.SwingUtilities;

public class Program {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new MainWindowEventLoop());
		
		SwingUtilities.invokeLater(new SecondWindowEventLoop());
		
		System.out.println(String.format("Main thread %1$d started.", Thread.currentThread().getId()));
	}
}