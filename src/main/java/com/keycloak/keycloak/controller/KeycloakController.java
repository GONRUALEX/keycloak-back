package com.keycloak.keycloak.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keycloak.keycloak.dto.UserDto;
import com.keycloak.keycloak.service.IKeycloakService;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin')")
public class KeycloakController {
	
	@Autowired
	private IKeycloakService keycloakService;
	
	@GetMapping("/search")
	public ResponseEntity<?> findAllUsers(){
		return ResponseEntity.ok(keycloakService.findAllUsers());
	}
	
	@GetMapping("/search/{username}")
	public ResponseEntity<?> findAllUsers(@PathVariable("username") String username){
		return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws URISyntaxException{
		String response = keycloakService.createUser(userDto);
		return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
	}
	
	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userDto){
		keycloakService.updateUser(userId, userDto);
		return ResponseEntity.ok("user update");
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId){
		keycloakService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}
}
