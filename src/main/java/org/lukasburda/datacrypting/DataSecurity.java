package org.lukasburda.datacrypting;

import javax.crypto.Cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Key;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class DataSecurity {

    public static final String PUBLIC_KEY_FILE = "public.keyfile";
    public static final String PRIVATE_KEY_FILE = "private.keyfile";
    private final String ALGORITHM = "RSA";
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
        try {
            File file = new File(filePath);
            byte[] temp = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(temp);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(cipherOperation, key);
            byte[] temp1 = cipher.doFinal(temp);
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + operation);
            fileOutputStream.write(temp1);
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Throwable cause) {
            throw new SecurityException("Crypto operation error", cause);
        }

    }

    private void saveKeyToFile(File file, BigInteger modulus, BigInteger exponent) {
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

    private void loadPublicKeyFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            BigInteger modulus = (BigInteger) objectInputStream.readObject();
            BigInteger exponent = (BigInteger) objectInputStream.readObject();
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

    private void loadPrivateKeyFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            BigInteger modulus = (BigInteger) objectInputStream.readObject();
            BigInteger exponent = (BigInteger) objectInputStream.readObject();
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
            e.printStackTrace();
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
