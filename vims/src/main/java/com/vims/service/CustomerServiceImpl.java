package com.vims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vims.dao.CustomerDao;
import com.vims.model.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao custdao;
	
	
	
	@Override
	public List<Customer> findByCustomerId(String customer_id) {
		
		return custdao.findByCustomerId(customer_id);
		
	}

	@Override
	public List<Customer> findAll() {
		
		return custdao.findAll();
	}

	@Override
	public Optional<Customer> findById(String customer_id) {
	
		return custdao.findById(customer_id);
	}

	@Override
	public void deleteById(String customer_id) {
		custdao.deleteById(customer_id);
		
	}

	@Override
	public Customer save(Customer customer) {
	
		return custdao.save(customer);
	}

}
