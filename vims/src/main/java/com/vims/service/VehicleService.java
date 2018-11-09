package com.vims.service;

import java.util.List;
import java.util.Optional;

import com.vims.model.VehicleRegistration;

public interface VehicleService {

	
	public List<VehicleRegistration> findByPolicyId(String policy_id);
	public List<VehicleRegistration> findAll();
	public  Optional<VehicleRegistration> findById(String policy_id);
	void deleteById(String policy_id);
	public VehicleRegistration save(VehicleRegistration vehicle);
	
}
