package com.vims.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vims.model.AccidentClaim;
import com.vims.model.Cancellation;
import com.vims.model.Customer;
import com.vims.model.TheftClaim;
import com.vims.model.VehicleRegistration;

public interface CustomerDao extends JpaRepository<Customer, String> {
	
//	@Query("from customer_registration cr where cr.customer_id like %:Customer_Id%")
//	Optional<Customer> findByCustomerId(@Param("Customer_Id") String customer_id);
	
	@Query("from vehicle_details vd where vd.cust.customer_id like %:Customer_Id%")
	List<VehicleRegistration> getVehicleDetails(@Param("Customer_Id") String customer_id);
	
	@Query("from theft_claim tc where tc.cust.customer_id like %:Customer_Id%")
	List<TheftClaim> getTheftClaimDetails(@Param("Customer_Id") String customer_id);
	
	@Query("from accident_claim ac where ac.cust.customer_id like %:Customer_Id%")
	List<AccidentClaim> getAccidentClaimDetails(@Param("Customer_Id") String customer_id);
	
	@Query("from cancellation_details cd where cd.cust.customer_id like %:Customer_Id%")
	List<Cancellation> getCancellationDetails(@Param("Customer_Id") String customer_id);
}
