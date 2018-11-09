package com.vims.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vims.model.DirectPay;

public interface DirectPaydao extends JpaRepository<DirectPay, String>{

	@Query("from direct_pay dp where dp.payment_id like %:payment_id%")
	List<DirectPay> findByPaymentId(@Param("payment_id") String payment_id);
}
