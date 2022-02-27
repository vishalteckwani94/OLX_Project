package com.zensar.olx.rest;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zensar.olx.bean.AdvertisementStatus;
import com.zensar.olx.bean.AdvertisementPost;
import com.zensar.olx.bean.Category;
import com.zensar.olx.bean.NewAdvertisementPostRequest;
import com.zensar.olx.bean.NewAdvertisementPostResponse;
import com.zensar.olx.bean.OLXUser;
import com.zensar.olx.service.AdvertisementPostService;

@RestController
public class AdvertisementPostController {

	@Autowired
	AdvertisementPostService service;

	@PostMapping("/advertise/{un}")
	public NewAdvertisementPostResponse add(@RequestBody NewAdvertisementPostRequest request,
			@PathVariable(name = "un") String userName) {
		AdvertisementPost post = new AdvertisementPost();
		post.setTitle(request.getTitle());
		post.setPrice(request.getPrice());
		post.setDescription(request.getDescription());

		int categoryId = request.getCategoryId();

		RestTemplate restTemplate = new RestTemplate();
		Category category;
		String url = "http://localhost:9052/advertise/getCategory/" + categoryId;
		category = restTemplate.getForObject(url, Category.class);
		post.setCategory(category);

		url = "http://localhost:9051/user/find/" + userName;
		OLXUser olXUser = restTemplate.getForObject(url, OLXUser.class);
		post.setOlxUser(olXUser);

		AdvertisementStatus advertisemantStatus = new AdvertisementStatus(1, "OPEN");
		post.setAdvertisementStatus(advertisemantStatus);

		AdvertisementPost advertisementPost = this.service.addAdvertisement(post);

		NewAdvertisementPostResponse response = new NewAdvertisementPostResponse();
		response.setId(advertisementPost.getId());
		response.setTitle(advertisementPost.getTitle());
		response.setPrice(advertisementPost.getPrice());
		response.setCategory(advertisementPost.getCategory().getName());
		response.setDescription(advertisementPost.getDescription());
		response.setUserName(advertisementPost.getOlxUser().getUserName());
		response.setCreatedDate(advertisementPost.getCreatedDate());
		response.setModifiedDate(advertisementPost.getModifieddDate());
		response.setStatus(advertisementPost.getAdvertisementStatus().getStatus());
		

		return response;
		

	}
	@PutMapping("/advertise/{aid}/{userName}")
	public NewAdvertisementPostResponse f2(@RequestBody NewAdvertisementPostRequest request,@PathVariable(name="aid")int id,@PathVariable(name="userName")String userName) {
		
		AdvertisementPost post=this.service.getAdvertisementById(id);
		
		post.setTitle(request.getTitle());
		post.setDescription(request.getDescription());
		post.setPrice(request.getPrice());
		
		RestTemplate restTemplate = new RestTemplate();
		
		Category category;
		String url = "http://localhost:9052/advertise/getCategory/" + request.getCategoryId();
		category = restTemplate.getForObject(url, Category.class);
		post.setCategory(category);
		
		url = "http://localhost:9051/user/find/" + userName;
		OLXUser olXUser = restTemplate.getForObject(url, OLXUser.class);
		post.setOlxUser(olXUser);
		
		url="http://localhost:9052/advertise/status/"+request.getStatusId();
		AdvertisementStatus advertisementStatus;
		advertisementStatus=restTemplate.getForObject(url, AdvertisementStatus.class);
		post.setAdvertisementStatus(advertisementStatus);
		
		AdvertisementPost advertisementPost=this.service.updateAdvertisement(post);//writing to db
		
		NewAdvertisementPostResponse postResponse;
		postResponse=new NewAdvertisementPostResponse();
		
		postResponse.setId(advertisementPost.getId());
		postResponse.setTitle(advertisementPost.getTitle());
		postResponse.setDescription(advertisementPost.getDescription());
		postResponse.setPrice(advertisementPost.getPrice());
		postResponse.setUserName(advertisementPost.getOlxUser().getUserName());
		postResponse.setCategory(advertisementPost.getCategory().getName());
		postResponse.setCreatedDate(advertisementPost.getCreatedDate());
		postResponse.setModifiedDate(advertisementPost.getModifieddDate());
		postResponse.setStatus(advertisementPost.getAdvertisementStatus().getStatus());
		

		return postResponse;
	}
	@GetMapping("/user/advertise/{userName}")
	public List<NewAdvertisementPostResponse> f3(@PathVariable(name = "userName") String userName) {
		List<AdvertisementPost> advPost = this.service.getAllAdvertisements();
		RestTemplate restTemplate = new RestTemplate();
		List<AdvertisementPost> filterList = new ArrayList<>();
		String url = "http://localhost:9051/user/find/" + userName;
		OLXUser olxUser = restTemplate.getForObject(url, OLXUser.class);

		for (AdvertisementPost post : advPost) {

			Category category;
			url = "http://localhost:9052/advertise/getCategory/" + post.getCategory().getId();
			category = restTemplate.getForObject(url, Category.class);
			post.setCategory(category);
			System.out.println("Category-------" + post);

			url = "http://localhost:9052/advertise/status/" + post.getAdvertisementStatus().getId();
			AdvertisementStatus advertisementStatus;
			advertisementStatus = restTemplate.getForObject(url, AdvertisementStatus.class);
			post.setAdvertisementStatus(advertisementStatus);
			System.out.println("AdvertisementStatus" + post);
			if (olxUser.getOlxUserId() == post.getOlxUser().getOlxUserId()) {
				post.setOlxUser(olxUser);
				filterList.add(post);
			}
		}
		System.out.println("List--------------" + filterList);

		List<NewAdvertisementPostResponse> responseList = new ArrayList<>();

		for (AdvertisementPost advertisementPost : filterList) {
			NewAdvertisementPostResponse postRespone = new NewAdvertisementPostResponse();
			postRespone.setId(advertisementPost.getId());
			postRespone.setTitle(advertisementPost.getTitle());
			postRespone.setDescription(advertisementPost.getDescription());
			postRespone.setPrice(advertisementPost.getPrice());
			postRespone.setUserName(advertisementPost.getOlxUser().getUserName());
			postRespone.setCategory(advertisementPost.getCategory().getName());
			postRespone.setCreatedDate(advertisementPost.getCreatedDate());
			postRespone.setModifiedDate(advertisementPost.getModifieddDate());
			postRespone.setStatus(advertisementPost.getAdvertisementStatus().getStatus());

			responseList.add(postRespone);
		}
		return responseList;

	}
	@GetMapping("/user/advertise/{un}/{adId}")
	public NewAdvertisementPostResponse getSingleAdvertisement(@PathVariable(name="un") String userName,
			@PathVariable(name="adId") int adId) {
		
		AdvertisementPost post=this.service.getAdvertisementById(adId);
		
		NewAdvertisementPostResponse response=new NewAdvertisementPostResponse();
		RestTemplate restTemplate=new RestTemplate();
		
		Category category=post.getCategory();
		String url="http://localhost:9052/advertise/getCategory/"+category.getId();
		category=restTemplate.getForObject(url, Category.class);
		response.setCategory(category.getName());
		response.setDescription(category.getDescription());
		response.setId(post.getId());
		response.setTitle(post.getTitle());
		response.setPrice(post.getPrice());
		response.setCreatedDate(post.getCreatedDate());
		response.setModifiedDate(post.getModifieddDate());
		
		OLXUser olxUser=post.getOlxUser();
		url="http://localhost:9051/user/"+olxUser.getOlxUserId();
		olxUser=restTemplate.getForObject(url, OLXUser.class);
		response.setUserName(olxUser.getUserName());
		
		AdvertisementStatus advertisementStatus=post.getAdvertisementStatus();
		
		url="http://localhost:9052/advertise/status/"+advertisementStatus.getId();
		advertisementStatus=restTemplate.getForObject(url, AdvertisementStatus.class);
		response.setStatus(advertisementStatus.getStatus());
		
		return response;
	}
	
	
	}
	 
