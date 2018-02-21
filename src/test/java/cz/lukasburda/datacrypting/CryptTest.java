/**package cz.lukasburda.datacrypting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CryptTest {

	private final String expectedText = "Testing program";
	private final String filePath = "program.testing";

	private File fileForTest;
	private DataSecurity dataSecurity;

	@Before
	public void before() {

		dataSecurity = new DataSecurity();
		fileForTest = new File(filePath);

		if (fileForTest.exists() == false) {

			try {

				fileForTest.createNewFile();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		try {

			FileOutputStream fileOutputStream = new FileOutputStream(fileForTest);
			fileOutputStream.write(expectedText.getBytes());
			fileOutputStream.close();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	@Test
	public void test() {

		try {
			
			dataSecurity.encrypt(filePath);
			dataSecurity.decrypt(filePath);

		} catch (SecurityException e) {

			e.printStackTrace();

		}
	}

	@After
	public void after() {

		if (fileForTest.exists()) {

			fileForTest.delete();

		} else {

			return;

		}

	}
}
**/