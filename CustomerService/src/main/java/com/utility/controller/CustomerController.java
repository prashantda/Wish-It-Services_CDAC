package com.utility.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.utility.entity.Customer;
import com.utility.service.CustomerService;
import java.util.*;
@RestController
@RequestMapping("/api/customer")
@CrossOrigin
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@PostMapping("/savecustomer")
	public Customer saveCustomer(@RequestBody Customer customer) {
		return customerService.saveCustomer(customer);
	}
	@GetMapping("/getcustomer/{id}")
	public Customer getCustomer(@PathVariable("id") long id) {
		return customerService.getCustomer(id);
	}
	
	@GetMapping("/getallcustomer")
	public List<Customer> getAllCustomers(){
		return customerService.getAllCustomers();
	}
}
