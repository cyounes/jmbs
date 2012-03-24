package jmbs.client.Graphics;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jmbs.client.ClientRequests;
import jmbs.client.CurrentUser;
import jmbs.client.HashPassword;
import jmbs.common.User;
import javax.swing.JSeparator;

public class ConnectionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8035127754370206526L;
	private JTextField emailTextField;
	private JPasswordField passwordField;
	private JLabel respLabel; /* this will be used to say to the user if there is a wrong password or email */
	/**
	 * the main window
	 */
	private MainWindow w;
	/**
	 * the connection frame, used to stop displaying it after a connection successed
	 */
	private ConnectionFrame cf;
	/**
	 * @wbp.nonvisual location=198,111
	 */
	/**
	 * Create the panel.
	 * @param w the main window
	 * @param cf the connection frame
	 */
	public ConnectionPanel(MainWindow w, ConnectionFrame cf) {
		this.w = w;
		this.cf = cf;
		JLabel lblConnectToJmbs = new JLabel("Connect to JMBS");
		lblConnectToJmbs.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JLabel lblEmailAdress = new JLabel("Email Adress:");
		
		JLabel lblPassword = new JLabel("Password:");
		
		emailTextField = new JTextField();
		emailTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkConnection();
			}
		});
		emailTextField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkConnection();
			}
		});
		
		respLabel = new JLabel(" "); /* by default */
		
		JCheckBox chckbxRememberMe = new JCheckBox("Remember me");

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkConnection();
			}
		});
		
		JSeparator separator = new JSeparator();
		
		JLabel lblRegister = new JLabel("<html>don't have account? <a href=\"http://google.com\">register</a> </html>");
		
		ImagePanel logopanel = new ImagePanel("./src/jmbs/client/img/jmbslogo_small.png");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxRememberMe)
							.addGap(144))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPassword)
							.addGap(34)
							.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEmailAdress)
							.addGap(12)
							.addComponent(emailTextField, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(146)
							.addComponent(lblConnectToJmbs, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
							.addGap(124))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(81)
					.addComponent(lblRegister, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnConnect)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(161)
					.addComponent(logopanel, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
					.addGap(144))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(respLabel, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(17)
					.addComponent(lblConnectToJmbs)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(logopanel, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(emailTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmailAdress))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(18)
					.addComponent(chckbxRememberMe)
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnConnect)
						.addComponent(lblRegister))
					.addGap(28)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(respLabel, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
					.addContainerGap())
		);
		setLayout(groupLayout);
		

	}
	
	/**
	 * verify the email
	 * @param mail adress mail
	 * @return true if the user has entred a right email adress
	 */
	private boolean verification (String mail) {
		boolean correctMail = Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", mail);
		return (correctMail);
	}
	
	/**
	 * verify the password length
	 * @param passwdLength the password length
	 * @return true if password length >= 4
	 */
	private boolean verification (int passwdLength) {
		boolean correctPasswd = (passwdLength >= 4);
		return (correctPasswd);
	}
	
	/**
	 * this methode update the response Label 'respLabel'
	 */
	private void checkConnection() {
		if (!verification(emailTextField.getText())) {
			respLabel.setText("Enter a valide email please!");
			respLabel.setForeground(Color.red);
			//putElement(0, 3, respLabel);
		}
		else if (!verification(passwordField.getPassword().length)) {
			respLabel.setText("password less than 4 chars !");
			respLabel.setForeground(Color.red);
			//putElement(0, 3, respLabel);
		}
		else {
			respLabel.setText("Connection to server...");
			respLabel.setForeground(new Color(0,100,0));
			
			try {
				User u = new ClientRequests().getConnection().connectUser(this.emailTextField.
						getText(), new HashPassword(listToString(passwordField.getPassword())).
						getHashed());
				if ( u.getId() != -1 && u.getId() != -2 ) {
					cf.setVisible(false);
					new CurrentUser(u);
					this.initMainWindow();
					this.getMainWindow().getFrame().setVisible(true);
					//System.out.println(new CurrentUser().get().toString());
				}
				else if ( u.getId() == -2){
					respLabel.setText("Wrong password, Please try again!");
					respLabel.setForeground(new Color(200,0,0));
				}
				else if ( u.getId() == -1){
					respLabel.setText("The email you entered does not belong to any account. ");
					respLabel.setForeground(new Color(200,0,0));
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//putElement(0, 3, respLabel);
			//System.out.println("Vrai");
			
		}
	}
	
	/**
	 * @return return the main windows of this program
	 */
	private MainWindow getMainWindow() {
		return w;
	}
	
	private void initMainWindow() {
		this.w = new MainWindow();
	}
	
	private String listToString(char[] list ) {
		String retStr = new String();
		for(char c: list) {
			retStr+=c;
		}
		return retStr;
	}
}