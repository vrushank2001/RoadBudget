package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dbModule.DBOperations;
import utilitiesModule.EmailOTPSender;

public class ForgotPasswordDialog extends JDialog {

	private JTextField emailField;
	private JTextField otpField;
	private JLabel statusLabel;
	private JLabel timerLabel;

	private String generatedOtp;
	private long otpTime;
	private Timer countdownTimer;
	private static final long OTP_VALIDITY_MS = 2 * 60 * 1000; // 2 minutes

	public ForgotPasswordDialog(JFrame parent) {
		super(parent, "Forgot Password", true);
	    setSize(450, 320);
	    setLocationRelativeTo(parent);

	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 5, 10);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    JLabel titleLabel = new JLabel("Reset Your Password", SwingConstants.CENTER);
	    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    panel.add(titleLabel, gbc);

	    // Email label
	    gbc.gridy++;
	    gbc.gridwidth = 1;
	    panel.add(new JLabel("Email / Username:"), gbc);

	    // Email input
	    emailField = new JTextField(15);
	    gbc.gridx = 1;
	    panel.add(emailField, gbc);

	    // Send OTP Button
	    gbc.gridy++;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	    JButton sendOtpBtn = new JButton("Send OTP");
	    panel.add(sendOtpBtn, gbc);

	    // OTP label
	    gbc.gridy++;
	    gbc.gridwidth = 1;
	    gbc.gridx = 0;
	    panel.add(new JLabel("Enter OTP:"), gbc);

	    // OTP input
	    otpField = new JTextField(15);
	    otpField.setEnabled(false);
	    gbc.gridx = 1;
	    panel.add(otpField, gbc);

	    // Verify OTP Button
	    gbc.gridy++;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	    JButton verifyOtpBtn = new JButton("Verify OTP");
	    verifyOtpBtn.setEnabled(false);
	    panel.add(verifyOtpBtn, gbc);

	    // Timer label
	    gbc.gridy++;
	    timerLabel = new JLabel(" ", SwingConstants.CENTER);
	    timerLabel.setForeground(Color.DARK_GRAY);
	    panel.add(timerLabel, gbc);

	    // Status label
	    gbc.gridy++;
	    statusLabel = new JLabel(" ", SwingConstants.CENTER);
	    statusLabel.setForeground(Color.BLUE);
	    panel.add(statusLabel, gbc);

	    add(panel);

	    sendOtpBtn.addActionListener(e -> {
	        String emailOrUsername = emailField.getText().trim();
	        if (emailOrUsername.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Please enter an email.");
	            return;
	        }
	        
	        if(!DBOperations.userExists(emailOrUsername)) {
	            JOptionPane.showMessageDialog(this, "Email/Username not found.");
	            return;
	        }

	        String email = DBOperations.getUserEmail(emailOrUsername);
	        
	        generatedOtp = EmailOTPSender.generateOTP();
	        boolean sent = EmailOTPSender.sendOTPEmail(email, generatedOtp);
	        if (sent) {
	            otpTime = System.currentTimeMillis();
	            otpField.setEnabled(true);
	            verifyOtpBtn.setEnabled(true);
	           
	            // Mask the email
	            String maskedEmail = email.substring(0, 3) + "**************" + email.substring(email.length() - 12);
	            statusLabel.setText("OTP sent to " + maskedEmail);
	            startCountdown();
	        } else {
	            statusLabel.setText("Failed to send OTP.");
	        }
	    });

	    verifyOtpBtn.addActionListener(e -> {
	        String inputOtp = otpField.getText().trim();
	        long now = System.currentTimeMillis();
	        if (now - otpTime > OTP_VALIDITY_MS) {
	            statusLabel.setText("OTP expired. Please request a new one.");
	            stopCountdown();
	        } else if (inputOtp.equals(generatedOtp)) {
	            statusLabel.setText("OTP verified. You can now reset your password.");
	            //Wait 4 seconds before opening the reset password dialog
	            try {
	                Thread.sleep(4000);
	            } catch (InterruptedException e1) {
	                e1.printStackTrace();
	            }
	            
	            new ResetPasswordDialog(parent, emailField.getText().trim()).setVisible(true);
	            dispose();
	        } else {
	            statusLabel.setText("Incorrect OTP. Try again.");
	        }
	    });
	}

	private void startCountdown() {
	    stopCountdown(); // reset if any
	    countdownTimer = new Timer();
	    countdownTimer.scheduleAtFixedRate(new TimerTask() {
	        long timeLeft = OTP_VALIDITY_MS;

	        @Override
	        public void run() {
	            SwingUtilities.invokeLater(() -> {
	                if (timeLeft > 0) {
	                    timerLabel.setText("OTP expires in: " + timeLeft / 1000 + " sec");
	                    timeLeft -= 1000;
	                } else {
	                    timerLabel.setText("OTP expired!");
	                    otpField.setEnabled(false);
	                    stopCountdown();
	                }
	            });
	        }
	    }, 0, 1000);
	}

	private void stopCountdown() {
	    if (countdownTimer != null) {
	        countdownTimer.cancel();
	    }
	}
}
