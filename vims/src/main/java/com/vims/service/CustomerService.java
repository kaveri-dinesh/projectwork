package com.vims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.vims.model.Customer;
import com.vims.model.VehicleRegistration;

public interface CustomerService {
	
	
	//public Optional<Customer> findByCustomerId(String customer_id);
	public List<Customer> findAll();
	public  Optional<Customer> findById(String customer_id);
	void deleteById(String customer_id);
	public Customer save(Customer customer);
	String findByCustomer(String username, String password);
	List<VehicleRegistration> getVehicleDetails( String customer_id);

}
