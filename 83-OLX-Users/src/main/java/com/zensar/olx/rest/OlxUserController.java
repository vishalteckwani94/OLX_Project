package com.zensar.olx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.bean.OLXUser;
import com.zensar.olx.service.OlxUserService;
@RestController
public class OlxUserController {
	@Autowired
	OlxUserService service;
	@PostMapping("/user")
	public OLXUser addOlxUser(@RequestBody OLXUser olxUser) {
		return this.service.addOlxUser(olxUser);
			
	}
	@GetMapping("/user/{uid}")
	public OLXUser findOlxUserById(@PathVariable(name="uid")int id) {
		return this.service.findOlxUser(id);
		
	}
	@GetMapping("/user/find/{userName}")
	public OLXUser findOlxUserByName(@PathVariable(name="userName")String name) {
		return this.service.findOlxUserByName(name);
	}
	

}
