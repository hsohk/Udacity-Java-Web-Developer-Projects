package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class HomeController {
    FileService fileService;
    UserService userService;
    NoteService noteService;
    CredentialService credentialService;
    EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomeView(Authentication authentication, Model model){
        model.addAttribute("files",fileService.getAllFiles(authentication.getName().toString()));
        model.addAttribute("notes",noteService.getAllNotes(authentication.getName().toString()));
        model.addAttribute("credentials",credentialService.getAllCredentials(authentication.getName().toString()));
        model.addAttribute("encryptionService",encryptionService);
        return "home";
    }

    ////////////////////////////////
    //File
    @PostMapping("/file-upload")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile multipartFile, Model model) throws IOException {
        String uploadError = null;
        if(!fileService.isFileNameAvailable(multipartFile.getOriginalFilename())){
            uploadError = "The filename already exists";
            model.addAttribute("fileerror", uploadError);
        }
        if(uploadError==null){
            int userid = userService.findUseridByName(authentication.getName().toString());
            int rowsAdded = fileService.store(multipartFile,userid);
            if(rowsAdded<0){
                uploadError = "There was an error uploading file. Please try again.";
            }
            if(uploadError == null){
                model.addAttribute("filesuccess",true);
            } else {
                model.addAttribute("fileerror", uploadError);
            }
        }
        model.addAttribute("files",fileService.getAllFiles(authentication.getName().toString()));
        return "result";
    }

    @GetMapping("/file/delete/{fileId}")
    public String deleteFile(Authentication authentication, @PathVariable int fileId, Model model) {
      if(fileService.delete(authentication.getName().toString(),fileId)<0){
            model.addAttribute("deleteError","Failed to delete");
        } else {
            model.addAttribute("deleteSuccess",true);
        }
        return "result";
    }

    @GetMapping("/file/view/{fileId}")
    public ResponseEntity<Resource>  fileView(@PathVariable int fileId, Model model){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.getFilename() + "\"").body(new
                        ByteArrayResource(file.getFiledata()));
    }

    //Note
    //Insert,Update
    @PostMapping("/note-create")
    public String createNote(@ModelAttribute Note newNote, Authentication authentication, Model model){
        String error = null;
        String username = authentication.getName().toString();
        int rowsAdded;
        if(newNote.getNoteid()==null || noteService.getNote(null,newNote.getNoteid())==null) {
            newNote.setUserid(userService.findUseridByName(username));
            rowsAdded = noteService.insertNote(newNote);
        } else {
            rowsAdded = noteService.updateNote(username, newNote);
        }

        if (rowsAdded < 0) {
            error = "There was an error saving note. Please try again.";
        }
        if (error == null) {
            model.addAttribute("noteSucess", true);
        } else {
            model.addAttribute("noteError", error);
        }
        model.addAttribute("notes",noteService.getAllNotes(authentication.getName().toString()));
        return "result";
    }

    //Delete
    @GetMapping("/note/delete/{noteid}")
    public String deleteNote(Authentication authentication, @PathVariable int noteid, Model model) {
        if(noteService.deleteNote(authentication.getName().toString(),noteid)<0){
            model.addAttribute("deleteError","Failed to delete");
        } else {
            model.addAttribute("deleteSuccess",true);
        }
        return "result";
    }

    //Credential
    @PostMapping("/credential-create")
    public String createNote(@ModelAttribute Credential userCredential, Authentication authentication, Model model){
        String error = null;
        String username = authentication.getName().toString();
        int rowsAdded;
        if(userCredential.getCredentialid()==null || credentialService.getCredential(userCredential.getCredentialid())==null) {
            userCredential.setUserid(userService.findUseridByName(username));
            rowsAdded = credentialService.insertCredential(userCredential);
        } else {
            rowsAdded = credentialService.updateCredential(username, userCredential);
        }

        if (rowsAdded < 0) {
            error = "There was an error saving note. Please try again.";
            model.addAttribute("credentialError", error);
        }
        if (error == null) {
            model.addAttribute("credentialSuccess", true);
        } else {
            model.addAttribute("credentialError", error);
        }
        model.addAttribute("credentials",credentialService.getAllCredentials(authentication.getName().toString()));
        return "result";
    }

    //Delete Credential
    @GetMapping("/credential/delete/{credentialid}")
    public String deleteCredential(Authentication authentication, @PathVariable int credentialid, Model model) {
        if(credentialService.deleteCredential(authentication.getName().toString(),credentialid)<0){
            model.addAttribute("deleteError","Failed to delete");
        } else {
            model.addAttribute("deleteSuccess",true);
        }
        return "result";
    }
}
