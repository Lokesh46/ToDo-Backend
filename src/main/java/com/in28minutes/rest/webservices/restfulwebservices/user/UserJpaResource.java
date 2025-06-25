package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.rest.webservices.restfulwebservices.todo.repository.UserRepository;

@RestController
public class UserJpaResource {
	
	private final UserRepository userRepository;

	public UserJpaResource(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@PostMapping("/user")
	public ResponseEntity<User> saveUser(@RequestBody User user){
		
//		Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
//	    
//	    if (userOptional.isPresent()) {
//	        // Conflict: user already exists
//	        return ResponseEntity.status(409).build();
//	    }

	    user.setId(null);  // Ensure it's a new entity
	    User savedUser = userRepository.save(user);

	    return ResponseEntity.ok(savedUser);
	}
	
	@GetMapping("/user")
	public List<User> getUser(){
		List<User> users= userRepository.findAll();
		return (users);
	}
}
