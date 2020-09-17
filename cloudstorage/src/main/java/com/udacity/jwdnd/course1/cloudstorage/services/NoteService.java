package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    NoteMapper noteMapper;
    UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public List<Note> getAllNotes(String username){
        return noteMapper.getAllNotes(userService.findUseridByName(username));
    }

    public Note getNote(String username, int noteid){
        return noteMapper.getNote(noteid);
    }

    public int deleteNote(String username, int noteid){
        if(userService.getUser(username).getUserid() != noteMapper.getNote(noteid).getUserid()) return -1;
        return noteMapper.deleteNote(noteid);
    }

    public int updateNote(String username, Note note){
        int userid = userService.getUser(username).getUserid();
        if(noteMapper.getNote(note.getNoteid()).getUserid() != userid)  return -1;
        return noteMapper.updateNote(note);
    }

    public int insertNote(Note note){
        return noteMapper.insertNote(note);
    }
}
