package com.vims.service;

import java.util.List;
import java.util.Optional;

import com.vims.model.Customer;

public interface CustomerService {
	
	
	public List<Customer> findByCustomerId(String customer_id);
	public List<Customer> findAll();
	public  Optional<Customer> findById(String customer_id);
	void deleteById(String customer_id);
	public Customer save(Customer customer);

}
