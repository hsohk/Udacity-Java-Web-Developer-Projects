package com.example.demo.controllers;

import com.example.demo.TestUnits;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUnits.injectObjects(userController,"userRepository",userRepo);
        TestUnits.injectObjects(userController,"cartRepository", cartRepo);
        TestUnits.injectObjects(userController,"bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser_happy_path() throws Exception{
        when(encoder.encode(any())).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void createUser_sad_path() throws Exception{
        when(encoder.encode(any())).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword_");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<User> response1 = userController.createUser(r);

        assertNotNull(response1);
        assertEquals(400,response1.getStatusCodeValue());

        r.setPassword("test");
        r.setConfirmPassword("test");
        final ResponseEntity<User> response2 = userController.createUser(r);

        assertNotNull(response2);
        assertEquals(400,response2.getStatusCodeValue());
    }

    @Test
    public void findByUsername_happy_path() throws Exception{
        User user = generateTestUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> response  = userController.findByUserName(user.getUsername());

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User responseUser = response.getBody();
        assertNotNull(responseUser);
        assertEquals(user,responseUser);
    }

    @Test
    public void findById_happy_path() throws Exception{
        User user = generateTestUser();
        when(userRepo.findById(user.getId())).thenReturn(java.util.Optional.ofNullable(user));
        final ResponseEntity<User> response  = userController.findById(user.getId());

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User responseUser = response.getBody();
        assertEquals(user,responseUser);
    }

    public User generateTestUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("ohk");
        return user;
    }
}
