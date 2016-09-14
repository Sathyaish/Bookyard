package bookyard.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class LoginDialog(private val title : String) : JDialog(null, title, ModalityType.APPLICATION_MODAL) {

    var lblTopSpace : JLabel? = null;
    var loginPanel : JPanel? = null;
    var statusPanel : JPanel? = null;
    var lblStatus : JLabel? = null;

    init {

        this.setTitle(title);

        Initialize();
    }

    override fun getTitle() : String {
        return this.title;
    }

    protected fun Initialize() {

        lblTopSpace = JLabel("Login into Bookyard");
        lblTopSpace!!.setForeground(this.getBackground());

        loginPanel = LoginPanel(this);
        statusPanel = JPanel();

        statusPanel!!.setBorder(BevelBorder(BevelBorder.LOWERED));
        statusPanel!!.setSize(this.getWidth(), 50);
        statusPanel!!.setLayout(BorderLayout());

        lblStatus = JLabel("Status");
        lblStatus!!.setFont(Font("Verdana", Font.PLAIN, 12));
        lblStatus!!.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel!!.add(lblStatus, BorderLayout.SOUTH);

        this.setLayout(BorderLayout());
        val container : Container = this.getContentPane();

        container.add(lblTopSpace, BorderLayout.NORTH);
        container.add(loginPanel, BorderLayout.CENTER);
        container.add(statusPanel, BorderLayout.SOUTH);

        this.pack();
    }

    public fun setStatusLabel(text : String?, color : Color ) {
        this.lblStatus!!.setForeground(color);
        this.lblStatus!!.setText(text);
        this.lblStatus!!.setToolTipText(text);
    }
}