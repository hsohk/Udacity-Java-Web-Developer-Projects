package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUserSignupLoginAndLogout() throws InterruptedException {
		//Signup
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("hs","ohk","testid","testpassword");
		Assertions.assertEquals("Login", driver.getTitle());

		//Login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("testid","testpassword");
		Assertions.assertEquals("Home", driver.getTitle());

		//Logout
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteCreationViewingEditingDeletion() throws InterruptedException {
		ResultPage resultPage = new ResultPage(driver);

		//Signup
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("noteusername","ohk","notetestid","notetestpassword");
		Assertions.assertEquals("Login", driver.getTitle());

		//Login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("notetestid","notetestpassword");
		Assertions.assertEquals("Home", driver.getTitle());

		//Create Note
		HomePage homePage = new HomePage(driver);
		homePage.createNote("TestNoteTitle", "TestNoteDescription");
		Thread.sleep(2000);
		Assertions.assertEquals("Result", driver.getTitle());
		resultPage.noteSaveContinue.click();
		Assertions.assertEquals("Home", driver.getTitle());
		Thread.sleep(2000);

		//Check the note ist
		Note note = homePage.getNote();
		Assertions.assertEquals("TestNoteTitle",note.getNotetitle());
		Assertions.assertEquals("TestNoteDescription",note.getNotedescription());

		//EDIT Note
		Note EditedNote= new Note(null, "EditedTitle", "EditedDescription", null);
		homePage.editNote(EditedNote);
		Thread.sleep(2000);
		Assertions.assertEquals("Result", driver.getTitle());
		resultPage.noteSaveContinue.click();
		Thread.sleep(2000);
		note = homePage.getNote();
		Assertions.assertEquals(EditedNote.getNotetitle(),note.getNotetitle());
		Assertions.assertEquals(EditedNote.getNotedescription(),note.getNotedescription());

		//DELETE Note
		homePage.deleteNote();
		Thread.sleep(2000);
		Assertions.assertEquals("Result", driver.getTitle());
		resultPage.deleteSuccessContinue.click();
		Thread.sleep(2000);
		Assertions.assertEquals("Home", driver.getTitle());
		homePage.notesTab.click();
		boolean listExist = homePage.checkTitleList();
		Thread.sleep(1500);
		Assertions.assertEquals(true,listExist);

		homePage.logout();
	}

	@Test
	public void testCredentialCreationViewingEditingDeletion() throws InterruptedException {
		ResultPage resultPage = new ResultPage(driver);
		//Signup
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("hs","ohk","credentialid","credentialpassword");

		//Login
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("credentialid","credentialpassword");


		//Create Credential
		HomePage homePage = new HomePage(driver);
		homePage.createCredential("TestUrl", "TestUsername","TestPassword");
		Thread.sleep(2000);
		Assertions.assertEquals("Result", driver.getTitle());
		resultPage.credentialSaveContinue.click();
		Thread.sleep(2000);
		Assertions.assertEquals("Home", driver.getTitle());

		//Check the Credentail ist
		Credential credential = homePage.getCredential();
		Assertions.assertEquals("TestUrl",credential.getUrl());
		Assertions.assertEquals("TestUsername",credential.getUsername());
		Assertions.assertNotEquals("TestPassword",credential.getPassword());

		//Edit Credential
		Note EditedNote= new Note(null, "EditedTitle", "EditedDescription", null);
		Credential editedCredential = new Credential(null, "EditedUrl", "EditedUsername", null,"EditedPassword", null);
		homePage.editCredential(editedCredential);
		Thread.sleep(2000);
		Assertions.assertEquals("Result", driver.getTitle());
		resultPage.credentialSaveContinue.click();
		Thread.sleep(2000);
		credential = homePage.getCredential();
		Assertions.assertEquals(editedCredential.getUrl(),credential.getUrl());
		Assertions.assertEquals(editedCredential.getUsername(),credential.getUsername());
		Assertions.assertNotEquals(editedCredential.getPassword(),credential.getPassword());

		//Delete Credential
		homePage.deleteCredential();
		Thread.sleep(2000);
		Assertions.assertEquals("Result", driver.getTitle());
		resultPage.deleteSuccessContinue.click();
		Thread.sleep(2000);
		boolean listExist = homePage.checkCredentialList();
		Assertions.assertEquals(true,listExist);

		homePage.logout();
	}



}
