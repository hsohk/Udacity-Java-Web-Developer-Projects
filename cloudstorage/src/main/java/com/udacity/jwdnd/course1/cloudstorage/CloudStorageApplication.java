package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudStorageApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudStorageApplication.class, args);
	}

/*	@Bean
	CommandLineRunner init(UserService userService){
		return (args) -> {
			User user = new User(null,"1",null,"1","1","1");
			userService.createUser(user);
		};
	}*/
}
