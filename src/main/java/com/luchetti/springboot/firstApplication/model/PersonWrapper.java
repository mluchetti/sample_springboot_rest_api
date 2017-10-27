package com.luchetti.springboot.firstApplication.model;

import java.util.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Component
@JsonRootName("people")
public class PersonWrapper {

	@JsonProperty("people")
	List<Person> personList = new ArrayList<Person>();
	
	public List<Person> getPeople() {
		return personList;
	}
	public Person getById(String id) {
		return personList.stream().filter(c-> c.getId().equals(id)).findFirst().orElse(null);
	}

}
