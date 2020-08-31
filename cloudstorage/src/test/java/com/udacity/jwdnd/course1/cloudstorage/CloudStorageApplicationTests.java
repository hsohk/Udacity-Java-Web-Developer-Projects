package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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
	public void testUserSignupLoginAndLogout(){
		//Signup
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("hs","ohk","testid","testpassword");

		//Login
		driver.get("http://localhost:" + this.port + "/login");
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
		//Signup
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("hs","ohk","testid","testpassword");

		//Login
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("testid","testpassword");
		Assertions.assertEquals("Home", driver.getTitle());

		//Create Note
		HomePage homePage = new HomePage(driver);
		homePage.createNote("TestNoteTitle", "TestNoteDescription");

		//Check the note ist
		Note note = homePage.getNote();
		Assertions.assertEquals("TestNoteTitle",note.getNotetitle());
		Assertions.assertEquals("TestNoteDescription",note.getNotedescription());

		//EDIT Note
		Note EditedNote= new Note(null, "EditedTitle", "EditedDescription", null);
		homePage.editNote(EditedNote);
		note = homePage.getNote();
		Assertions.assertEquals(EditedNote.getNotetitle(),note.getNotetitle());
		Assertions.assertEquals(EditedNote.getNotedescription(),note.getNotedescription());

		//DELETE Note
		homePage.deleteNote();
		boolean listExist = homePage.checkTitleList();
		Assertions.assertEquals(true,listExist);
	}

	@Test
	public void testCredentialCreationViewingEditingDeletion() throws InterruptedException {
		//Signup
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("hs","ohk","testid","testpassword");

		//Login
		driver.get("http://localhost:" + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("testid","testpassword");
		Assertions.assertEquals("Home", driver.getTitle());

		//Create Credential
		HomePage homePage = new HomePage(driver);
		homePage.createCredential("TestUrl", "TestUsername","TestPassword");

		//Check the Credentail ist
		Credential credential = homePage.getCredential();
		Assertions.assertEquals("TestUrl",credential.getUrl());
		Assertions.assertEquals("TestUsername",credential.getUsername());
		Assertions.assertNotEquals("TestPassword",credential.getPassword());

		//Edit Credential
		Note EditedNote= new Note(null, "EditedTitle", "EditedDescription", null);
		Credential editedCredential = new Credential(null, "EditedUrl", "EditedUsername", null,"EditedPassword", null);
		homePage.editCredential(editedCredential);
		credential = homePage.getCredential();
		Assertions.assertEquals(editedCredential.getUrl(),credential.getUrl());
		Assertions.assertEquals(editedCredential.getUsername(),credential.getUsername());
		Assertions.assertNotEquals(editedCredential.getPassword(),credential.getPassword());

		//Delete Credential
		homePage.deleteCredential();
		boolean listExist = homePage.checkCredentialList();
		Assertions.assertEquals(true,listExist);


	}



}
