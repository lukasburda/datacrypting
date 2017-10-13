package cz.lukasburda.datacrypting;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CryptoFrame {

	private JFrame frame;
	private JFileChooser fileChooser;
	private DataSecurity dataSecurity;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CryptoFrame window = new CryptoFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CryptoFrame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 412, 104);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		dataSecurity = new DataSecurity();

		Panel appPanel = new Panel();
		frame.getContentPane().add(appPanel, BorderLayout.CENTER);
		appPanel.setLayout(null);

		TextField fileDirField = new TextField();
		fileDirField.setText("Direction of file");
		fileDirField.setBounds(10, 10, 301, 22);
		appPanel.add(fileDirField);

		Button getFileButton = new Button("Get File");
		getFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				fileDirField.setText(fileChooser.getSelectedFile().toString());
			}
		});
		getFileButton.setBounds(326, 10, 70, 22);
		appPanel.add(getFileButton);

		Button encryptFileButton = new Button("Encrypt");
		encryptFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataSecurity.encrypt(fileDirField.getText());
					JOptionPane.showMessageDialog(frame, "File was encrypted successfully!");
				} catch (SecurityException exception) {
					JOptionPane.showMessageDialog(frame, "Encryption error!", "Error", JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();
				}
			}
		});
		encryptFileButton.setBounds(10, 43, 70, 22);
		appPanel.add(encryptFileButton);

		Button decryptFileButton = new Button("Decrypt");
		decryptFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataSecurity.decrypt(fileDirField.getText());
					JOptionPane.showMessageDialog(frame, "File was decrypted successfully!");
				} catch (SecurityException exception) {
					JOptionPane.showMessageDialog(frame, "Decryption error!", "Error", JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();
				}
			}
		});
		decryptFileButton.setBounds(86, 43, 70, 22);
		appPanel.add(decryptFileButton);
	}
}
