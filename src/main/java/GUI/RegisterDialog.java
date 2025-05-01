package GUI;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import dbModule.DBOperations;
import utilitiesModule.SecurityUtils;

public class RegisterDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegisterDialog dialog = new RegisterDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegisterDialog() {
		JDialog registerDialog = new JDialog(this, "Register", true);
	    registerDialog.setSize(350, 250);
	    registerDialog.setLocationRelativeTo(this);
	    registerDialog.setLayout(new GridBagLayout());

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Username
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    registerDialog.add(new JLabel("Username:"), gbc);

	    gbc.gridx = 1;
	    JTextField usernameField = new JTextField(15);
	    registerDialog.add(usernameField, gbc);

	    // Password
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    registerDialog.add(new JLabel("Password:"), gbc);

	    gbc.gridx = 1;
	    JPasswordField passwordField = new JPasswordField(15);
	    registerDialog.add(passwordField, gbc);

	    // Confirm Password
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    registerDialog.add(new JLabel("Confirm Password:"), gbc);

	    gbc.gridx = 1;
	    JPasswordField confirmPasswordField = new JPasswordField(15);
	    registerDialog.add(confirmPasswordField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    registerDialog.add(new JLabel("Email:"), gbc);
	    
	    gbc.gridx = 1;
	    JTextField emailField = new JTextField(15);
	    registerDialog.add(emailField, gbc);
	    
	    //Show Password CheckBox
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
	    registerDialog.add(showPasswordCheckBox, gbc);
	    
	    // Register Button
	    gbc.gridx = 1;
	    gbc.gridy = 5;
	    gbc.anchor = GridBagConstraints.CENTER;
	    JButton registerBtn = new JButton("Register");
	    registerDialog.add(registerBtn, gbc);

	   
	    // Show/hide password feature
	    showPasswordCheckBox.addActionListener(e -> {
	        if (showPasswordCheckBox.isSelected()) {
	            passwordField.setEchoChar((char) 0);
	            confirmPasswordField.setEchoChar((char) 0);
	        } else {
	            passwordField.setEchoChar('*');
	            confirmPasswordField.setEchoChar('*');
	        }
	    });
	    
	    // Enter key to register
	    registerDialog.getRootPane().setDefaultButton(registerBtn);
	    
	    // Close dialog on ESC key
	    registerDialog.getRootPane().registerKeyboardAction(e -> registerDialog.dispose(),
	        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	        JComponent.WHEN_IN_FOCUSED_WINDOW);
	    
	    // Register logic
	    registerBtn.addActionListener(event -> {
	        String username = usernameField.getText();
	        String password = new String(passwordField.getPassword());
	        String confirmPassword = new String(confirmPasswordField.getPassword());
	        String email = emailField.getText();

	        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || emailField.getText().isEmpty()) {
	            JOptionPane.showMessageDialog(registerDialog, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else if (!password.equals(confirmPassword)) {
	            JOptionPane.showMessageDialog(registerDialog, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            if (DBOperations.userExists(username) || DBOperations.emailExists(emailField.getText())) {
	                JOptionPane.showMessageDialog(registerDialog, "Username or email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }

	            String salt = SecurityUtils.getSalt();
	            String hashedPassword = SecurityUtils.hashPassword(password, salt);

	            DBOperations.insertAccount(username, hashedPassword, salt, email);
	            JOptionPane.showMessageDialog(registerDialog, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
	            registerDialog.dispose();
	        }
	    });


	    registerDialog.setVisible(true);
	}

}
