package com.vims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.vims.model.AccidentClaim;
import com.vims.model.DirectPay;
import com.vims.model.RegisteredPay;
import com.vims.model.TheftClaim;
import com.vims.model.VehicleRegistration;

public interface VehicleService {

	
	public List<VehicleRegistration> findByPolicyId(String policy_id);
	public List<VehicleRegistration> findAll();
	public  Optional<VehicleRegistration> findById(String policy_id);
	void deleteById(String policy_id);
	public VehicleRegistration save(VehicleRegistration vehicle);
	List<DirectPay> getDirectPayDetails(@Param("policy_id") String policy_id);
	List<RegisteredPay> getRegisteredPayDetails(@Param("policy_id") String policy_id);
	List<TheftClaim> getTheftClaimDetails(@Param("policy_id") String policy_id);
	List<AccidentClaim> getAccidentClaimDetails(@Param("policy_id") String policy_id);
}
