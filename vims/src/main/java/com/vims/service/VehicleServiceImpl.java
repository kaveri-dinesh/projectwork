package com.vims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vims.dao.VehicleDao;
import com.vims.model.VehicleRegistration;

@Service
public class VehicleServiceImpl implements VehicleService{

	
	@Autowired
	private VehicleDao vehicledao;
	
	@Override
	public List<VehicleRegistration> findByPolicyId(String policy_id) {
		
		return vehicledao.findByPolicyId(policy_id);
	}

	@Override
	public List<VehicleRegistration> findAll() {
		// TODO Auto-generated method stub
		return vehicledao.findAll();
	}

	@Override
	public Optional<VehicleRegistration> findById(String policy_id) {
		// TODO Auto-generated method stub
		return vehicledao.findById(policy_id);
	}

	@Override
	public void deleteById(String policy_id) {
		vehicledao.deleteById(policy_id);
		
	}

	@Override
	public VehicleRegistration save(VehicleRegistration vehicle) {
		// TODO Auto-generated method stub
		return vehicledao.save(vehicle);
	}

}
