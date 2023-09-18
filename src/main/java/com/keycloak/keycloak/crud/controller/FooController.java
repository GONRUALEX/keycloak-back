package com.keycloak.keycloak.crud.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keycloak.keycloak.crud.controller.dto.ResponseMessage;
import com.keycloak.keycloak.crud.model.Foo;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/foo")
@CrossOrigin("*")
public class FooController {

	List<Foo> foos = Stream.of(new Foo(1,"foo1"),new Foo(3,"foo3"),new Foo(2,"foo2")).collect(Collectors.toList());
	
	@GetMapping("/list")
	public ResponseEntity<List<Foo>> lista(){
		return new ResponseEntity(foos, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	@RolesAllowed("user")
	public ResponseEntity<Foo> detail(@PathVariable("id") int id){
		Foo foo = foos.stream().filter(f->f.getId()==id).findFirst().orElse(null);
		return new ResponseEntity(foo, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	@RolesAllowed("admin")
	public ResponseEntity<?> create(@RequestBody Foo foo){
		int maxIndex = foos.stream().max(Comparator.comparing(m->m.getId())).get().getId();
		foo.setId(maxIndex+1);
		foos.add(foo);
		return new ResponseEntity(new ResponseMessage("creado"), HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@RolesAllowed("admin")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Foo foo){
		Foo fooUpdate = foos.stream().filter(f->f.getId()==id).findFirst().orElse(null);
		fooUpdate.setName(foo.getName());
		foos.add(fooUpdate);
		return new ResponseEntity(new ResponseMessage("udate"), HttpStatus.OK);
	}
}
