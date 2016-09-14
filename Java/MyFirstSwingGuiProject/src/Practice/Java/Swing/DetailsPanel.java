package Practice.Java.Swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DetailsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected JLabel lblName;
	protected JLabel lblOccupation;
	protected JTextField txtName;
	protected JTextField txtOccupation;
	protected JButton btnAdd;
	protected GridBagConstraints constraints;
	
	public DetailsPanel() {
		super();
		
		Initialize();
	}

	private void Initialize() {
		
		// Set preferred width
		Dimension dimension = this.getPreferredSize();
		dimension.width = 250;
		this.setPreferredSize(dimension);
		
		// Set the border of the panel
		this.setBorder(BorderFactory.createTitledBorder("Personal Details"));
		
		// Create controls
		this.lblName = new JLabel("Name: ");
		this.lblOccupation = new JLabel("Occupation: ");
		this.txtName = new JTextField(10);
		this.txtOccupation = new JTextField(10);
		this.btnAdd = new JButton("Add");
		
		// Create a layout manager -- I'll use the GridLayout
		this.setLayout(new GridBagLayout());
		
		// Put the controls on the manager using a GridLayoutContstraints object
		this.constraints = new GridBagConstraints();
		
		// 3 rows, 2 columns
		// Name label: First row, first column
		constraints.weighty = 0.1;
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(lblName, constraints);
		
		// Name text field: First row, second column
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(txtName, constraints);
		
		// Occupation label: Second row, first column
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(lblOccupation, constraints);
		
		// Occupation text field: Second row, second column
		constraints.gridx = 1;
		constraints.gridy = 1;
		this.add(txtOccupation, constraints);
		
		// Add button: third row, second column
		constraints.weighty = 2;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridx = 1;
		constraints.gridy = 2;
		this.add(btnAdd, constraints);
	}
	
	public void addAddButtonActionListener(ActionListener listener) {
		
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		
		this.btnAdd.addActionListener(listener);
	}
	
	public void removeAddButtonActionListener(ActionListener listener) {
		
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		
		this.btnAdd.removeActionListener(listener);
	}
}
