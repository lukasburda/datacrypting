package org.lukasburda.datacrypting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataSecurityTest {

    private final String publicKeyPath = "./src/test/resources/keys/public.keyfile";
    private final String privateKeyPath = "./src/test/resources/keys/private.keyfile";
    private final String fileToEncrypt = "./src/test/resources/testDocument.txt";
    private final String decryptedResult = "This is so easy";

    private DataSecurity dataSecurity;

    @Before
    public void before() {
        dataSecurity = new DataSecurity();
    }

    @Test
    public void testCrypto() throws SecurityException, IOException {
        dataSecurity.encrypt(fileToEncrypt, publicKeyPath, privateKeyPath);
        dataSecurity.decrypt(fileToEncrypt + "-encrypted", privateKeyPath);

        File resultFile = new File(fileToEncrypt + "-encrypted-decrypted");
        FileReader fileReader = new FileReader(resultFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String resultFromFile = bufferedReader.readLine();
        Assert.assertEquals(decryptedResult, resultFromFile);
    }
}
