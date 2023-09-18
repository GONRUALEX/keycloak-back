package com.keycloak.keycloak.service;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import com.keycloak.keycloak.dto.UserDto;



public interface IKeycloakService {

	List<UserRepresentation> findAllUsers();
	List<UserRepresentation> searchUserByUsername(String username);
	String createUser(UserDto userDto);
	void deleteUser(String userId);
	void updateUser(String userId, UserDto userDto);
}
