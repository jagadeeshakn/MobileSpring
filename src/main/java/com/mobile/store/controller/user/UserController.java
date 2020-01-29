package com.mobile.store.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobile.store.model.User;
import com.mobile.store.repo.UserRepository;
import com.mobile.store.service.SecretKeyGenerator;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SecretKeyGenerator SecretKeyGenerator;
    

    @GetMapping("/users")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
    	User user = (userRepository.findById(id)).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: "+id));
        return ResponseEntity.ok().body(user);
    }

    @CrossOrigin
    @PostMapping("/user")
    public ResponseEntity<User> createEmployee(@Valid @RequestBody User user) {
    	System.out.println("user creation request body...."+ user);
    	User user1 = userRepository.save(user);
    	System.out.println("user created...."+ user1);
        return ResponseEntity.ok().body(user1);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateEmployee(@PathVariable(value="id") Long id, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
    	User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: "+id));

    	user.setEmail(userDetails.getEmail());
    	user.setFirstname(userDetails.getFirstname());
    	user.setLastname(userDetails.getLastname());
    	user.setUsername(userDetails.getUsername());
    	user.setPassword(userDetails.getPassword());
    	user.setEmail(userDetails.getEmail());
    	user.setCountry(userDetails.getCountry());
        final User updatedEmployee = userRepository.save(user);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value="id") Long id) throws ResourceNotFoundException{
    	User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: "+id));

    	userRepository.delete(user);
        Map<String , Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @CrossOrigin
    @PostMapping("/login")
    public User userLogin(@Valid @RequestBody User userData) {
    	User user = userRepository.findByUsername(userData.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found for this userData :: "+userData));
    	if(userData.getPassword().equals(user.getPassword())) {
    		System.out.println("User looged in"+ user);
    		return user;
    	}else {
    		System.out.println("User looged in failed"+ userData);
    	 throw new ResourceNotFoundException("Incorrect Username or Password");	
    	}
    }
    
    @CrossOrigin
    @PostMapping("/encryptionKey")
    public String encryptionKey(@Valid @RequestParam String userName) {
    	return SecretKeyGenerator.genereateUserSecretkey(userName);
    }
}
