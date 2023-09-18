package com.keycloak.keycloak.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruevaController {

	@GetMapping("/hello-1")
	@PreAuthorize("hasRole('admin')")
	public String hello1() {
		return "hello 1";
	}
	
	@GetMapping("/hello-2")
	@PreAuthorize("hasRole('user') or hasRole('admin')")
	public String hello2() {
		return "hello 2";
	}
}
