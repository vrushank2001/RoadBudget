package GUI;

import javax.swing.*;
import javax.swing.border.*;

import dbModule.DBOperations;
import utilitiesModule.SecurityUtils;

import java.awt.*;

public class MainPage extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPage frame = new MainPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainPage() {
	    setTitle("Road Budget App");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 450, 300);
	    setLocationRelativeTo(null);

	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	    setContentPane(mainPanel);

	    // Top section: Welcome Label
	    JLabel welcomeLabel = new JLabel("Welcome to the Road Budget App", SwingConstants.CENTER);
	    welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(20.0f));
	    mainPanel.add(welcomeLabel, BorderLayout.NORTH);

	    // Center section: Login form
	    JPanel formPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Username
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    formPanel.add(new JLabel("Username:"), gbc);

	    gbc.gridx = 1;
	    JTextField usernameField = new JTextField(15);
	    formPanel.add(usernameField, gbc);

	    // Password
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    formPanel.add(new JLabel("Password:"), gbc);

	    gbc.gridx = 1;
	    JPasswordField passwordField = new JPasswordField(15);
	    formPanel.add(passwordField, gbc);

	    // Show password checkbox
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
	    formPanel.add(showPasswordCheckBox, gbc);

	    // Buttons panel
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
	    JButton loginButton = new JButton("Login");
	    JButton registerButton = new JButton("Register");
	    buttonPanel.add(loginButton);
	    buttonPanel.add(registerButton);
	    formPanel.add(buttonPanel, gbc);

	    mainPanel.add(formPanel, BorderLayout.CENTER);

	    // Show/hide password feature
	    showPasswordCheckBox.addActionListener(e -> {
	        if (showPasswordCheckBox.isSelected()) {
	            passwordField.setEchoChar((char) 0);
	        } else {
	            passwordField.setEchoChar('*');
	        }
	    });
	    
	    loginButton.addActionListener(e -> {
	        String username = usernameField.getText();
	        String password = new String(passwordField.getPassword());

	        if (username.isEmpty() || password.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
	            return;
	        }

	        boolean authenticated = DBOperations.authenticateUser(username, password);

	        if (authenticated) {
	            JOptionPane.showMessageDialog(this, "Login successful!");
	            // proceed to dashboard
	            new Dashboard(username).setVisible(true);
	            this.dispose();
	        } else {
	            JOptionPane.showMessageDialog(this, "Invalid username or password.");
	        }
	    });
	    
	    registerButton.addActionListener(e -> showRegisterDialog());
	}
	
	private void showRegisterDialog() {
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

	    //Show Password CheckBox
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
	    registerDialog.add(showPasswordCheckBox, gbc);
	    
	    // Register Button
	    gbc.gridx = 1;
	    gbc.gridy = 4;
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
	    
	    // Register logic
	    registerBtn.addActionListener(event -> {
	        String username = usernameField.getText();
	        String password = new String(passwordField.getPassword());
	        String confirmPassword = new String(confirmPasswordField.getPassword());

	        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
	            JOptionPane.showMessageDialog(registerDialog, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else if (!password.equals(confirmPassword)) {
	            JOptionPane.showMessageDialog(registerDialog, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            if (DBOperations.userExists(username)) {
	                JOptionPane.showMessageDialog(registerDialog, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }

	            String salt = SecurityUtils.getSalt();
	            String hashedPassword = SecurityUtils.hashPassword(password, salt);

	            DBOperations.insertAccount(username, hashedPassword, salt);
	            JOptionPane.showMessageDialog(registerDialog, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
	            registerDialog.dispose();
	        }
	    });


	    registerDialog.setVisible(true);
	}



}
