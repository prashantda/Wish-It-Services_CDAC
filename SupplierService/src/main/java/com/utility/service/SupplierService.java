package com.utility.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.utility.entity.ServiceType;
import com.utility.entity.Supplier;
import com.utility.model.User;
import com.utility.repository.SupplierRepository;
import com.utility.valueobjects.Customer;
import com.utility.valueobjects.ResponseTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
@Service
public class SupplierService {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ServiceTypeService sts;
	
	private static final String SUPPLIER_SERVICE= "SupplierService";

	public Supplier saveSupplier(Supplier supplier) {
		
		return supplierRepository.save(supplier);
	}

	public Supplier getSupplier(long id) {
		
		return supplierRepository.findById(id).get();
	}
	
	public List<Supplier> getAllSupplier() {
		
		return supplierRepository.findAll();
	}
//	@CircuitBreaker(name = SUPPLIER_SERVICE, fallbackMethod = "getscFallback")
	@Retry(name = SUPPLIER_SERVICE,fallbackMethod ="getscFallback" )
	public ResponseTemplate getsc(long id) {
		ResponseTemplate rt=new ResponseTemplate();		
		Supplier s= supplierRepository.findById(id).get();
//		Customer c=restTemplate.getForObject("http://CUSTOMER-SERVICE/api/customer/getcustomer/"+s.getCustomersid(),Customer.class);
//		rt.setCustomer(c);
		rt.setSupplier(s);
		return rt;
	}
	
	public ResponseTemplate getscFallback(Exception e) {
		ResponseTemplate rt=new ResponseTemplate();
		return rt;
	}


	
	public User getSupplierUser(String auth) {
		HttpHeaders http=new HttpHeaders();
		http.add("Authorization",auth);
		HttpEntity entity=new HttpEntity(http); 
		HttpEntity response=restTemplate.exchange("http://SECURITY-SERVICE/api/secure/getsupplieruser", HttpMethod.GET, entity, User.class);
		return (User) response.getBody();
	}
	public User getCustomerUser(String auth) {
		System.out.println("getCustomerUser in");
		HttpHeaders http=new HttpHeaders();
		http.add("Authorization",auth);
		HttpEntity entity=new HttpEntity(http); 
		HttpEntity response=restTemplate.exchange("http://SECURITY-SERVICE/api/secure/getcustomeruser", HttpMethod.GET, entity, User.class);
		System.out.println("getCustomerUser out"+response.getBody());
		return (User) response.getBody();
	}
	
}
//public String about(String auth) {
//HttpHeaders http=new HttpHeaders();
//http.add("Authorization",auth);
//HttpEntity entity=new HttpEntity(http); 
//HttpEntity response=restTemplate.exchange("http://SECURITY-SERVICE/api/secure/about", HttpMethod.GET, entity, String.class);
//return (String) response.getBody();		
//}
