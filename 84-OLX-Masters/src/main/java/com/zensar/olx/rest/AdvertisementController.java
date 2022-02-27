package com.zensar.olx.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.bean.AdvertisementStatus;
import com.zensar.olx.bean.Category;
import com.zensar.olx.service.AdvertiseStatusService;
import com.zensar.olx.service.CategoryService;

@RestController
public class AdvertisementController {
	@Autowired
	AdvertiseStatusService service;
	
	@PostMapping("/advertise/addAdvertiseStatus")
	public AdvertisementStatus addAdvertiseStatus(@RequestBody AdvertisementStatus advertiementStatus) {
		return this.service.addAdvertisementStatus(advertiementStatus);
		
	}
	@GetMapping("/advertise/advertiseStatus")
	public List<AdvertisementStatus> getAlladvertiseStatus(){
		return this.service.getAllAdvertisementStatus();
	}
	
	@GetMapping("/advertise/status/{id}")
	public AdvertisementStatus findAdvertisementStatusById(@PathVariable(name="id")int  id) {
		return this.service.findAdvertisementStatus(id);
	}
}
