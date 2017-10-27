package com.luchetti.springboot.firstApplication.service;

import java.util.List;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import com.luchetti.springboot.firstApplication.model.*;

@Service
@RefreshScope
public class PersonService {
	static Logger l = LogManager.getLogger(PersonService.class);

	@Autowired
	PersonWrapper pw;

	@Value("${prop.env}")
	private String env;

	public PersonService() {
	}
	
	public PersonWrapper getPw() {
		return pw;
	}

	public void setPw(PersonWrapper pw) {
		this.pw = pw;
	}

	public Person getPerson(String id) {
		l.debug("Env: {}: Retrieve person by id: {}", env, id);
		Person p = pw.getById(id);
		l.info("First Name: {}", (p == null ? "Person Not Found" : p.getfName()));
		return p;
	}
	
	public List<Person> getPersons() {
		l.debug("Env: {}: Retrieve All people", env);
		return pw.getPeople();
	}

}
