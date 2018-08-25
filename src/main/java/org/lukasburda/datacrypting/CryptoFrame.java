package org.lukasburda.datacrypting;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Panel;
import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.Button;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CryptoFrame {

    private final String FILE_TEXT = "Direction of file";
    private JFrame frmRsaEncryptordecryptor;
    private DataSecurity dataSecurity;

    public CryptoFrame() {
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CryptoFrame window = new CryptoFrame();
                    window.frmRsaEncryptordecryptor.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initialize() {
        frmRsaEncryptordecryptor = new JFrame();
        frmRsaEncryptordecryptor.setTitle("RSA Encryptor/Decryptor");
        frmRsaEncryptordecryptor.setResizable(false);
        frmRsaEncryptordecryptor.setBounds(100, 100, 425, 158);
        frmRsaEncryptordecryptor.setLocationRelativeTo(null);
        frmRsaEncryptordecryptor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dataSecurity = new DataSecurity();

        Panel appPanel = new Panel();
        frmRsaEncryptordecryptor.getContentPane().add(appPanel, BorderLayout.CENTER);
        appPanel.setLayout(null);

        TextField fileDirField = new TextField();
        fileDirField.setText(FILE_TEXT);
        fileDirField.setBounds(10, 10, 301, 22);
        appPanel.add(fileDirField);

        Button getFileButton = new Button("Get File");
        getFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(null);
                if (fileChooser.getSelectedFile() != null)
                    fileDirField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        getFileButton.setBounds(326, 10, 83, 22);
        appPanel.add(getFileButton);

        TextField publicKeyFileField = new TextField();
        publicKeyFileField.setBounds(10, 38, 301, 22);
        publicKeyFileField.setText(DataSecurity.PUBLIC_KEY_FILE);
        appPanel.add(publicKeyFileField);

        TextField privateKeyFileField = new TextField();
        privateKeyFileField.setBounds(10, 66, 301, 22);
        privateKeyFileField.setText(DataSecurity.PRIVATE_KEY_FILE);
        appPanel.add(privateKeyFileField);

        Button encryptFileButton = new Button("Encrypt");
        encryptFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dataSecurity.encrypt(fileDirField.getText(), publicKeyFileField.getText(),
                            privateKeyFileField.getText());
                    JOptionPane.showMessageDialog(frmRsaEncryptordecryptor, "File was encrypted successfully!");
                    fileDirField.setText(FILE_TEXT);
                } catch (SecurityException exception) {
                    JOptionPane.showMessageDialog(frmRsaEncryptordecryptor, "Encryption error!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                }
            }
        });
        encryptFileButton.setBounds(8, 97, 70, 22);
        appPanel.add(encryptFileButton);

        Button decryptFileButton = new Button("Decrypt");
        decryptFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dataSecurity.decrypt(fileDirField.getText(), privateKeyFileField.getText());
                    JOptionPane.showMessageDialog(frmRsaEncryptordecryptor, "File was decrypted successfully!");
                    fileDirField.setText(FILE_TEXT);
                } catch (SecurityException exception) {
                    JOptionPane.showMessageDialog(frmRsaEncryptordecryptor, "Decryption error!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    exception.printStackTrace();
                }
            }
        });
        decryptFileButton.setBounds(84, 97, 70, 22);
        appPanel.add(decryptFileButton);

        Button getPublicKeyButton = new Button("Get Pub. Key");
        getPublicKeyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(null);
                if (jFileChooser.getSelectedFile() != null)
                    publicKeyFileField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        getPublicKeyButton.setBounds(326, 38, 83, 22);
        appPanel.add(getPublicKeyButton);

        Button getPrivateKeyButton = new Button("Get Priv. Key");
        getPrivateKeyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(null);
                if (jFileChooser.getSelectedFile() != null)
                    publicKeyFileField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        getPrivateKeyButton.setBounds(326, 66, 83, 22);
        appPanel.add(getPrivateKeyButton);
    }
}
