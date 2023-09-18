package com.keycloak.keycloak.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.keycloak.keycloak.dto.UserDto;
import com.keycloak.keycloak.util.KeycloakProvider;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KeyCloakServiceImpl implements IKeycloakService{

	@Override
	public List<UserRepresentation> findAllUsers() {
		return KeycloakProvider.getRealmResource()
				.users().list();
	}

	@Override
	public List<UserRepresentation> searchUserByUsername(String username) {
		// TODO Auto-generated method stub
		return KeycloakProvider.getRealmResource().users().searchByUsername(username,true);
	}

	@Override
	public String createUser(@NonNull UserDto userDto) {
		int status = 0;
		UsersResource userResource = KeycloakProvider.getUserResource();
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName(userDto.getFirstName());
		userRepresentation.setLastName(userDto.getLastName());
		userRepresentation.setEmail(userDto.getEmail());
		userRepresentation.setUsername(userDto.getUsername());
		userRepresentation.setEmailVerified(true);
		userRepresentation.setEnabled(true);
		
		Response response = userResource.create(userRepresentation);
		
		status = response.getStatus();
		
		if (status==201) {
			String path = response.getLocation().getPath();
			String userId = path.substring(path.lastIndexOf("/")+1);
			CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
			credentialRepresentation.setTemporary(false);
			credentialRepresentation.setType(OAuth2Constants.PASSWORD);
			credentialRepresentation.setValue(userDto.getPassword());
			userResource.get(userId).resetPassword(credentialRepresentation);
			
			RealmResource realmResource = KeycloakProvider.getRealmResource();
			List<RoleRepresentation> roleRepresentation = null;
			if(userDto.getRoles()==null || userDto.getRoles().isEmpty()) {
				roleRepresentation = List.of(realmResource.roles().get("realm_user").toRepresentation());
			}else {
				roleRepresentation = realmResource.roles().list().stream().
						filter(role->
						userDto
						.getRoles()
						.stream()
						.anyMatch(
								roleName-> 
								roleName.equalsIgnoreCase(role.getName())
								)
						).toList();
			}
			
			realmResource.users().get(userId).roles().realmLevel().add(roleRepresentation);
			return "User create satisfayerrrr";
		}else if(status==409) {
			log.error("El usuario ya existe bribón");
			return("ya existe el usuario bribón");
		}else {
			log.error("El usuario está tomando un café,,,, no molestar)");
			return("yeah");
		}
	}

	@Override
	public void deleteUser(String userId) {
		KeycloakProvider.getUserResource().get(userId).remove();
		
	}

	@Override
	public void updateUser(String userId, UserDto userDto) {
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setType(OAuth2Constants.PASSWORD);
		credentialRepresentation.setValue(userDto.getPassword());
		
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName(userDto.getFirstName());
		userRepresentation.setLastName(userDto.getLastName());
		userRepresentation.setEmail(userDto.getEmail());
		userRepresentation.setUsername(userDto.getUsername());
		userRepresentation.setEmailVerified(true);
		userRepresentation.setEnabled(true);
		userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
		
		UserResource userResource = KeycloakProvider.getUserResource().get(userId);
		userResource.update(userRepresentation);
	}
	
	

}
