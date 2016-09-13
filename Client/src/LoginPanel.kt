package bookyard.client;

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

import bookyard.contracts.IAuthenticationManager;
import bookyard.contracts.OperationResult;

open public class LoginPanel(var containerDialog : LoginDialog) : JPanel() {

    var lblUserName : JLabel? = null;
    var lblPassword : JLabel? = null;
    var txtUserName : JTextField? = null;
    var txtPassword : JPasswordField? = null;
    var btnLogin : JButton? = null;
    var btnCancel : JButton? = null;

    init {
        this.Initialize();
    }

    open protected fun Initialize() {

        this.setLayout(GridBagLayout());
        val constraints : GridBagConstraints = GridBagConstraints();

        // Create controls
        lblUserName = JLabel("User Name: ");
        lblPassword = JLabel("Password: ");
        txtUserName = JTextField(15);
        txtPassword = JPasswordField(15);
        txtPassword!!.echoChar =  '*';
        btnLogin = JButton("Login");
        btnCancel = JButton("Cancel");

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

        btnLogin!!.addActionListener(object : ActionListener {

            override fun actionPerformed(e : ActionEvent) {

                val userName : String = txtUserName!!.getText();
                val password : String = String(txtPassword!!.getPassword());
                val appId : String = "BookyardClient_123";
                val appSecret : String = "Auth0 is awesome!";

                // Ask a component to authenticate the user
                val authMgr : IAuthenticationManager<String> = APIAuthenticationManager();
                val result : OperationResult<String> = authMgr.authenticateUser(userName, password, appId, appSecret);

                if (result.successful) {
                    // if the user is good, we close the
                    // login dialog and load the new form
                    containerDialog.setStatusLabel(null, Color.black);
                    containerDialog.dispose();

                    val accessToken : String? = result.data;

                    val bookRecommendationsFrame : JFrame = BookRecommendationsFrame(accessToken);
                    bookRecommendationsFrame.setSize(500, 500);
                    bookRecommendationsFrame.setVisible(true);
                }
                else {
                    // otherwise, we display the error message we
                    // received from the API server
                    containerDialog.setStatusLabel(result.errorMessage, Color.red);
                }
            }
        });

        btnCancel!!.addActionListener(object : ActionListener {

            override public fun actionPerformed(e : ActionEvent) {

                containerDialog.dispose();
            }
        });
    }
}