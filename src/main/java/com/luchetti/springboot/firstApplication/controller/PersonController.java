package com.luchetti.springboot.firstApplication.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luchetti.springboot.firstApplication.model.*;
import com.luchetti.springboot.firstApplication.service.PersonService;

@RestController
@RequestMapping("/persons")
public class PersonController {

	@Autowired
	PersonService ps;
	ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	
	@RequestMapping("")
	public List<Person> getPeople() {
		return ps.getPersons();
	}
	
	@RequestMapping(path="",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonWrapper> setPeople(@RequestBody String payload) throws JsonParseException, JsonMappingException, IOException {
		
		PersonWrapper pw = objectMapper.readValue(payload, PersonWrapper.class);
		ps.getPersons().addAll(pw.getPeople());
//		return  new ResponseEntity<PersonWrapper>(ps.getPw(), HttpStatus.OK);
		return  new ResponseEntity<PersonWrapper>(pw, HttpStatus.OK);
	}
	
	@RequestMapping("/{id}")
	public Person getPerson(@PathVariable("id") String id) {
		return ps.getPerson(id);
	}
}
