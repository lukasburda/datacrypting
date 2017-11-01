package cz.lukasburda.datacrypting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
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

import javax.crypto.Cipher;

public class DataSecurity {

	private static final String ALGORITHM = "RSA";
	public static final String PUBLIC_KEY_FILE = "public.keyfile";
	public static final String PRIVATE_KEY_FILE = "private.keyfile";
	private final String ENCRYPTED = "-encrypted";
	private final String DECRYPTED = "-decrypted";

	private KeyFactory keyFactory;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public DataSecurity() {

		try {
			keyFactory = KeyFactory.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public void encrypt(String filePath, String publicKeyFileDir, String privateKeyFileDir) throws SecurityException {
		File publicKeyFile = new File(publicKeyFileDir);

		if (publicKeyFile.exists()) {
			loadPublicKeyFile(publicKeyFile);
		} else {
			File privateKeyFile = new File(privateKeyFileDir);
			generateKeys(publicKeyFile, privateKeyFile);
		}

		doCrypto(filePath, ENCRYPTED, Cipher.ENCRYPT_MODE, publicKey);
	}

	public void decrypt(String filePath, String privateKeyFileDir) throws SecurityException {
		loadPrivateKeyFile(new File(privateKeyFileDir));
		doCrypto(filePath, DECRYPTED, Cipher.DECRYPT_MODE, privateKey);
	}

	private void doCrypto(String filePath, String operation, int cipherOperation, Key key) throws SecurityException {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;

		try {
			File file = new File(filePath);
			byte[] temp = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(temp);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(cipherOperation, key);
			byte[] temp1 = cipher.doFinal(temp);
			fileOutputStream = new FileOutputStream(filePath + operation);
			fileOutputStream.write(temp1);
		} catch (Throwable cause) {
			throw new SecurityException("Crypto operation error", cause);
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void saveKeyToFile(File file, BigInteger modulus, BigInteger exponent) {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(modulus);
			objectOutputStream.writeObject(exponent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void loadPublicKeyFile(File file) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		
		try {
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);
			BigInteger modulus = (BigInteger) objectInputStream.readObject();
			BigInteger exponent = (BigInteger) objectInputStream.readObject();
			RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
		} catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				objectInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadPrivateKeyFile(File file) {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;

		try {
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);
			BigInteger modulus = (BigInteger) objectInputStream.readObject();
			BigInteger exponent = (BigInteger) objectInputStream.readObject();
			RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
			privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
		} catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				objectInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void generateKeys(File publicKeyFile, File privateKeyFile) {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
			RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveKeyToFile(publicKeyFile, rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent());
			saveKeyToFile(privateKeyFile, rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
}
