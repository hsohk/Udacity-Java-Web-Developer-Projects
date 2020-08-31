package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    HashService hashService;
    UserMapper userMapper;

    public UserService(HashService hashService, UserMapper userMapper) {
        this.hashService = hashService;
        this.userMapper = userMapper;
    }

    public boolean isUserNameAvailable(String username){
        return userMapper.getUser(username)==null;
    }

    public int findUseridByName(String username) {
        User user = userMapper.getUser(username);
        if(user==null) return -1;
        else return user.getUserid();
    }
    public int createUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodeSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(),encodeSalt);
        return userMapper.insertUser(new User(null, user.getUsername(), encodeSalt,hashedPassword,user.getFirstname(),user.getLastname()));
    }

    public User getUser(String username){
        return userMapper.getUser(username);
    }
}
