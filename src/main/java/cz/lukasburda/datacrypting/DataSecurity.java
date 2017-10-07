package cz.lukasburda.datacrypting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

public class DataSecurity {

	private static final String ALGORITHM = "RSA";
	private static final String PUBLIC_KEY_FILE = "public.keyfile";
	private static final String PRIVATE_KEY_FILE = "private.keyfile";
	
	private KeyFactory keyFactory;
	private RSAPublicKeySpec rsaPublicKeySpec;
	private RSAPrivateKeySpec rsaPrivateKeySpec;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public DataSecurity(){
		File publicKeyFile = new File(PUBLIC_KEY_FILE);
		File privateKeyFile = new File(PRIVATE_KEY_FILE);
		try {
			keyFactory = KeyFactory.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if(publicKeyFile.exists() && privateKeyFile.exists()){
			loadPublicKeyFile(publicKeyFile);
			loadPrivateKeyFile(privateKeyFile);
		} else{
			generateKeys(publicKeyFile, privateKeyFile);	    	
		}
	}

	public void encrypt(String filePath){
		
		doCrypto(filePath, filePath + "-encrypted",Cipher.ENCRYPT_MODE, publicKey);
	}
	
	public void decrypt(String filePath){
		doCrypto(filePath, filePath + "-decrypted",Cipher.DECRYPT_MODE, privateKey);
	}
	
	private void doCrypto(String filePath, String outputFilePath ,int operation, Key key) {
		File file = new File(filePath);
        byte[] temp = new byte[(int) file.length()];
        
         try {
        	 FileInputStream fileInputStream = new FileInputStream(file);
	         fileInputStream.read(temp);
	         Cipher cipher = Cipher.getInstance(ALGORITHM);
	         cipher.init(operation, key);
			 byte[] temp1 = cipher.doFinal(temp);
	         FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFilePath));
	         fileOutputStream.write(temp1);
	         fileInputStream.close();
	         fileOutputStream.close();
	         JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Success!");
		} catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
	        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Operation error!", "Error", JOptionPane.ERROR_MESSAGE);    
		}
         
	}
	
	private void saveKeyToFile(File file, BigInteger modulus, BigInteger exponent){
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(modulus);
			objectOutputStream.writeObject(exponent);
			fileOutputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadPublicKeyFile(File file){
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			BigInteger modulus = (BigInteger) objectInputStream.readObject();
			BigInteger exponent = (BigInteger) objectInputStream.readObject();
			rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
			objectInputStream.close();
			fileInputStream.close();
		} catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
	}
	private void loadPrivateKeyFile(File file){
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			BigInteger modulus = (BigInteger) objectInputStream.readObject();
			BigInteger exponent = (BigInteger) objectInputStream.readObject();
			rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
			privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
			objectInputStream.close();
			fileInputStream.close();
		} catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
	}
	
	private void generateKeys(File publicKeyFile, File privateKeyFile){
    	try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGenerator.initialize(2048);
	        KeyPair keyPair = keyPairGenerator.generateKeyPair();
	        privateKey = keyPair.getPrivate();
	        publicKey = keyPair.getPublic();
	        rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
	        rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
	        saveKeyToFile(publicKeyFile, rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent());
	        saveKeyToFile(privateKeyFile, rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
}
