package Practice.Java.Swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	protected ActionListener detailsPanelAddButtonActionListener;
	protected ActionListener clickMeButtonActionListener;
	protected DetailsPanel detailsPanel;
	protected JTextArea textArea;
	protected JButton btnClickMe;
	

	public MainWindow(String title) {
		super(title);
		
		Initialize();
	}

	protected void Initialize() {
		
		// set the layout
		this.setLayout(new BorderLayout());
		
		// create controls
		this.detailsPanel = new DetailsPanel();
		this.textArea = new JTextArea();
		this.btnClickMe = new JButton("Click me!");
		
		// add controls to the content pane / container 
		Container container = this.getContentPane();
		
		container.add(detailsPanel, BorderLayout.WEST);
		container.add(textArea, BorderLayout.CENTER);
		container.add(btnClickMe, BorderLayout.PAGE_END); // or SOUTH
		
		this.clickMeButtonActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append("Hello\n");
			}
		};
		
		btnClickMe.addActionListener(this.clickMeButtonActionListener);
		
		detailsPanelAddButtonActionListener = new AddButtonActionListener();
		this.detailsPanel.addAddButtonActionListener(detailsPanelAddButtonActionListener);
	}
	
	@Override
	public void dispose() {
		
		this.btnClickMe.removeActionListener(this.clickMeButtonActionListener);
		
		this.detailsPanel.removeAddButtonActionListener(detailsPanelAddButtonActionListener);
		
		super.dispose();
	}
	
}
