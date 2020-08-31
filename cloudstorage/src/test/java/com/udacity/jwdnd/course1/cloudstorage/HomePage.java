package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(id="logout")
    private WebElement logoutButton;

    //Note
    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="buttonNewNote")
    private WebElement newNoteButton;

    @FindBy(id="note-title")
    private WebElement noteTitleInput;

    @FindBy(id="note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id="noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(id = "note-edit")
    private WebElement noteEditButton;

    @FindBy(id = "note-delete")
    private WebElement noteDeleteButton;

    @FindBy(className = "listnotetitle")
    private WebElement noteTitleList;

    @FindBy(className = "listnotedescription")
    private WebElement noteDescriptionList;

    //Credential
    @FindBy(id="nav-credentials-tab")
    private WebElement credentialtab;

    @FindBy(id="button-new-credential")
    private WebElement newCredentialButton;

    @FindBy(id="credential-url")
    private WebElement credentialUrl;

    @FindBy(id="credential-username")
    private WebElement credentialUsername;

    @FindBy(id="credential-password")
    private WebElement credentialPassword;

    @FindBy(id="credentialSubmit")
    private WebElement credentialSubmit;

    @FindBy(className = "list-credential-url")
    private WebElement credentialListUrl;

    @FindBy(className = "list-credential-username")
    private WebElement credentialListUsername;

    @FindBy(className = "list-credential-password")
    private WebElement credentialListPassword;

    @FindBy(id = "credential-edit")
    private WebElement credentialEditButton;

    @FindBy(id = "credential-delete")
    private WebElement credentialDeleteButton;

    private WebDriver webDriver;

    public HomePage(WebDriver webDriver){
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout(){
        this.logoutButton.submit();;
    }

    public void createNote(String title, String description) throws InterruptedException {
        Thread.sleep(1000);
        notesTab.click();
        Thread.sleep(1000);
        newNoteButton.click();
        Thread.sleep(1000);
        noteTitleInput.sendKeys(title);
        noteDescriptionInput.sendKeys(description);
        noteSubmitButton.submit();
        Thread.sleep(1000);
    }

    public Note getNote() throws InterruptedException {
        Note note= new Note();
        Thread.sleep(2000);
        notesTab.click();
        Thread.sleep(2000);
        note.setNotetitle(noteTitleList.getText());
        note.setNotedescription(noteDescriptionList.getText());
        return note;
    }

    public boolean checkTitleList() throws InterruptedException {
        Thread.sleep(2000);
        notesTab.click();
        Thread.sleep(2000);
        return webDriver.findElements(By.cssSelector("listnotetitle")).isEmpty();
    }

    public void deleteNote() throws InterruptedException {
        Thread.sleep(2000);
        notesTab.click();
        Thread.sleep(2000);
        noteDeleteButton.click();
    }

    public void editNote(Note note) throws InterruptedException {
        noteEditButton.click();
        Thread.sleep(1000);
        noteTitleInput.clear();
        noteTitleInput.sendKeys(note.getNotetitle());
        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(note.getNotedescription());
        noteSubmitButton.submit();

    }

    public void createCredential(String url,String username, String password) throws InterruptedException {
        Thread.sleep(1000);
        credentialtab.click();
        Thread.sleep(1000);
        newCredentialButton.click();
        Thread.sleep(1000);
        credentialUrl.sendKeys(url);
        credentialUsername.sendKeys(username);
        credentialPassword.sendKeys(password);
        credentialSubmit.submit();
        Thread.sleep(1000);
    }

    public Credential getCredential() throws InterruptedException {
        Thread.sleep(2000);
        credentialtab.click();
        Thread.sleep(2000);
        Credential credential= new Credential(null,credentialListUrl.getText(),credentialListUsername.getText(),null,credentialListPassword.getText(),null);
        return credential;
    }

    public void editCredential(Credential credential) throws InterruptedException {
        credentialEditButton.click();
        Thread.sleep(1000);
        credentialUrl.clear(); credentialUrl.sendKeys(credential.getUrl());
        credentialUsername.clear(); credentialUsername.sendKeys(credential.getUsername());
        credentialPassword.clear(); credentialPassword.sendKeys(credential.getPassword());
        credentialSubmit.submit();
    }

    public void deleteCredential() throws InterruptedException {
        Thread.sleep(1000);
        credentialtab.click();
        Thread.sleep(1000);
        credentialDeleteButton.click();
    }

    public boolean checkCredentialList() throws InterruptedException {
        Thread.sleep(2000);
        credentialtab.click();
        Thread.sleep(2000);
        return webDriver.findElements(By.cssSelector("list-credential-username")).isEmpty();
    }
}
