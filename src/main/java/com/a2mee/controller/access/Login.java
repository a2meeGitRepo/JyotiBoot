package com.a2mee.controller.access;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.model.access.UserDetails;
import com.a2mee.services.access.LoginService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.login)
@CrossOrigin
public class Login {	
	

	@Autowired
	LoginService loginService;	
	
	@PostMapping(API.authenticate)
	public ResponseEntity<UserDetails> serviceLogin(@RequestHeader("id") String id,
			@RequestHeader("password") String password) {
	System.out.println("id pass==="+id+password);
			UserDetails returnbody = loginService.getUser(id, password);
			UserDetails user = new UserDetails();		
			System.out.println("id pass==="+returnbody);

			if (returnbody != null) {
				String x=returnbody.getId();
				user.setFirstName(returnbody.getFirstName());
				user.setLastName(returnbody.getLastName());
				user.setId(x);
				return new ResponseEntity<UserDetails>(user,HttpStatus.OK);
			}
			else {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
		}
	
	}
