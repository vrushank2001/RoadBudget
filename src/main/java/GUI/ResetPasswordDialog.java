package GUI;

import java.awt.*;

import javax.swing.*;

import javax.swing.border.*;

import dbModule.DBOperations;
import utilitiesModule.SecurityUtils;

public class ResetPasswordDialog extends JDialog {
	private JPasswordField newPasswordField;
	private JPasswordField confirmPasswordField;
	private JLabel statusLabel;

	public ResetPasswordDialog(JFrame parent, String emailOrUsername) {
	    super(parent, "Reset Password", true);
	    setSize(400, 300);
	    setLocationRelativeTo(parent);

	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 5, 10);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Title
	    JLabel title = new JLabel("Set New Password", SwingConstants.CENTER);
	    title.setFont(new Font("SansSerif", Font.BOLD, 18));
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    panel.add(title, gbc);

	    // New Password
	    gbc.gridy++;
	    gbc.gridwidth = 1;
	    panel.add(new JLabel("New Password:"), gbc);

	    gbc.gridx = 1;
	    newPasswordField = new JPasswordField(15);
	    panel.add(newPasswordField, gbc);

	    // Confirm Password
	    gbc.gridy++;
	    gbc.gridx = 0;
	    panel.add(new JLabel("Confirm Password:"), gbc);

	    gbc.gridx = 1;
	    confirmPasswordField = new JPasswordField(15);
	    panel.add(confirmPasswordField, gbc);

	    // Submit Button
	    gbc.gridy++;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	    JButton submitBtn = new JButton("Reset Password");
	    panel.add(submitBtn, gbc);

	    // Status Label
	    gbc.gridy++;
	    statusLabel = new JLabel("", SwingConstants.CENTER);
	    panel.add(statusLabel, gbc);

	    add(panel);

	    submitBtn.addActionListener(e -> {
	        String newPass = new String(newPasswordField.getPassword());
	        String confirmPass = new String(confirmPasswordField.getPassword());

	        if (newPass.isEmpty() || confirmPass.isEmpty()) {
	            statusLabel.setText("Password fields cannot be empty.");
	            statusLabel.setForeground(Color.RED);
	            return;
	        }

	        if (!newPass.equals(confirmPass)) {
	            statusLabel.setText("Passwords do not match.");
	            statusLabel.setForeground(Color.RED);
	            return;
	        }

	        if (newPass.length() < 6) {
	            statusLabel.setText("Password must be at least 6 characters.");
	            statusLabel.setForeground(Color.RED);
	            return;
	        }

	        try {
	            String salt = SecurityUtils.getSalt();
	            String hashedPassword = SecurityUtils.hashPassword(newPass, salt);

	            boolean updated = DBOperations.updateUserPassword(emailOrUsername, hashedPassword, salt);

	            if (updated) {
	                statusLabel.setForeground(new Color(0, 128, 0));
	                statusLabel.setText("Password successfully updated.");
	                JOptionPane.showMessageDialog(this, "Your password has been reset.");
	                dispose();
	            } else {
	                statusLabel.setForeground(Color.RED);
	                statusLabel.setText("Failed to update password.");
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            statusLabel.setForeground(Color.RED);
	            statusLabel.setText("Error: " + ex.getMessage());
	        }
	    });
	}
}
