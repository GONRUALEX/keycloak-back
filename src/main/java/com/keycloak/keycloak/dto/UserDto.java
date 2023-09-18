package com.keycloak.keycloak.dto;

import java.util.Set;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class UserDto {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private Set<String> roles;
}
