package practice.bookyard.client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import practice.bookyard.contracts.IAuthenticationManager;
import practice.bookyard.contracts.OperationResult;

public class LoginPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected JLabel lblUserName;
	protected JLabel lblPassword;
	protected JTextField txtUserName;
	protected JPasswordField txtPassword;
	protected JButton btnLogin;
	protected JButton btnCancel;
	protected LoginDialog containerDialog;

	public LoginPanel(LoginDialog containerDialog) {
		super();
		
		this.containerDialog = containerDialog;
		this.Initialize();
	}
	
	protected void Initialize() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		// Create controls
		lblUserName = new JLabel("User Name: ");
		lblPassword = new JLabel("Password: ");
		txtUserName = new JTextField(15);
		txtPassword = new JPasswordField(15);
		txtPassword.setEchoChar('*');
		btnLogin = new JButton("Login");
		btnCancel = new JButton("Cancel");
		
		// Set them on the panel using the constraints
		// 3 rows, 3 columns
		
		// lblUserName
		constraints.anchor = GridBagConstraints.FIRST_LINE_END;
		constraints.weighty = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(lblUserName, constraints);
		
		// txtUserName
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		this.add(txtUserName, constraints);
		
		// lblPassword
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		this.add(lblPassword, constraints);
		
		// txtPassword
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		this.add(txtPassword, constraints);
		
		// btnLogin
		constraints.anchor = GridBagConstraints.FIRST_LINE_END;
		constraints.weighty = 0.8;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		this.add(btnLogin, constraints);
		
		// btnCancel
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		this.add(btnCancel, constraints);
		
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String userName = txtUserName.getText();
				String password = new String(txtPassword.getPassword());
				String appId = "BookyardClient_123";
				String appSecret = "Auth0 is awesome!";
				
				// Ask a component to authenticate the user
				IAuthenticationManager<String> authMgr = new APIAuthenticationManager();
				OperationResult<String> result = authMgr.authenticateUser(userName, password, 
						appId, appSecret);
				
				if (result.getSuccessful()) {
					// if the user is good, we close the 
					// login dialog and load the new form
					containerDialog.setStatusLabel(null, Color.black);
					containerDialog.dispose();
					
					String accessToken = result.getData();
					
					JFrame bookRecommendationsFrame = new BookRecommendationsFrame(accessToken);
					bookRecommendationsFrame.setSize(500, 500);
					bookRecommendationsFrame.setVisible(true);
				}
				else {
					// otherwise, we display the error message we
					// received from the API server
					containerDialog.setStatusLabel(result.getErrorMessage(), Color.red);
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				containerDialog.dispose();
			}
		});
	}
}
