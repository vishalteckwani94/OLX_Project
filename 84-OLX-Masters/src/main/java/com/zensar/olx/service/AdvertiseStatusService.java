package com.zensar.olx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zensar.olx.bean.AdvertisementStatus;
import com.zensar.olx.bean.Category;
import com.zensar.olx.db.AdvertiesmentStatusDAO;

@Service
public class AdvertiseStatusService {
	@Autowired
	AdvertiesmentStatusDAO dao;
	
	public AdvertisementStatus addAdvertisementStatus(AdvertisementStatus advertiementStatus) {
		return this.dao.save(advertiementStatus);
		
	}
	public List<AdvertisementStatus> getAllAdvertisementStatus() {
		return this.dao.findAll();

	}
	public AdvertisementStatus findAdvertisementStatus(int id) {
		Optional<AdvertisementStatus> optional;
		optional=this.dao.findById(id);
		if(optional.isPresent())
			return optional.get();
		else
			return null;
	}
	
}