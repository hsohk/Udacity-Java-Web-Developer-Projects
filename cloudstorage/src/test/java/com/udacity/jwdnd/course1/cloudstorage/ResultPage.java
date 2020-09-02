package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(id="file-save-continue")
    public WebElement fileSaveContinue;

    @FindBy(id="file-error-continue")
    public WebElement fileErrorContinue;

    @FindBy(id="note-save-continue")
    public WebElement noteSaveContinue;

    @FindBy(id="note-error-continue")
    public WebElement noteErrorContinue;

    @FindBy(id="credential-save-continue")
    public WebElement credentialSaveContinue;

    @FindBy(id="credential-error-continue")
    public WebElement credentialErrorContinue;

    @FindBy(id="delete-success-continue")
    public WebElement deleteSuccessContinue;

    @FindBy(id="delete-error-continue")
    public WebElement deleteErrorContinue;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }


}
