package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileService {
    FileMapper fileMapper;
    UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public File getFileByName(String filename){
        return fileMapper.getFileByName(filename);
    }

    public File getFileById(int fileId){
        return fileMapper.getFileById(fileId);
    }

    public boolean isFileNameAvailable(String filename){
        return fileMapper.getFileByName(filename)==null;
    }

    public int store(MultipartFile multipartFile, Integer userid) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        File file = new File(null,
                multipartFile.getOriginalFilename(),
                /*filename.substring(filename.lastIndexOf('.')),*/multipartFile.getContentType().toString(),
                Long.toString(multipartFile.getSize()),
                userid,
                multipartFile.getBytes());

        return fileMapper.insertFile(file);
    }

    public List<File> getAllFiles(String username){
        return fileMapper.getAllFiles(userService.findUseridByName(username));
    }

    public int delete(String username,int fileId){
        if(userService.getUser(username).getUserid() != fileMapper.getFileById(fileId).getUserid()) return -1;
        return fileMapper.delete(fileId);
    }
}
