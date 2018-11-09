package com.vims.service;

import java.util.List;
import java.util.Optional;

import com.vims.model.AccidentClaim;
import com.vims.model.DirectPay;

public interface AccidentClaimService {

	public List<AccidentClaim> findByClaimId(String claim_id);
	public List<AccidentClaim> findAll();
	public  Optional<AccidentClaim> findById(String claim_id);
	void deleteById(String claim_id);
	public AccidentClaim save(AccidentClaim ac);
}
