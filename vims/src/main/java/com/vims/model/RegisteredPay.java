package com.vims.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="registered_pay")
@GenericGenerator(name="paymentid",strategy="com.vims.generators.PaymentIdGenerator")
public class RegisteredPay {

	@Id
	@GeneratedValue(generator="paymentid")
	private String payment_id;
	private String premium_amount;
	
	private Date due_date;
	private String paid_date;
	private String payment_mode;
	private String pay_amount;
	private String next_due_date;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="policy_id")
	private VehicleRegistration vehicle;

	public RegisteredPay(String payment_id, String premium_amount, Date due_date, String paid_date, String payment_mode,
			String pay_amount, String next_due_date) {
		
		this.payment_id = payment_id;
		this.premium_amount = premium_amount;
		this.due_date = due_date;
		this.paid_date = paid_date;
		this.payment_mode = payment_mode;
		this.pay_amount = pay_amount;
		this.next_due_date = next_due_date;
	}

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	public String getPremium_amount() {
		return premium_amount;
	}

	public void setPremium_amount(String premium_amount) {
		this.premium_amount = premium_amount;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public String getPaid_date() {
		return paid_date;
	}

	public void setPaid_date(String paid_date) {
		this.paid_date = paid_date;
	}

	public String getPayment_mode() {
		return payment_mode;
	}

	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getNext_due_date() {
		return next_due_date;
	}

	public void setNext_due_date(String next_due_date) {
		this.next_due_date = next_due_date;
	}

	public VehicleRegistration getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleRegistration vehicle) {
		this.vehicle = vehicle;
	}

	public RegisteredPay() {
		
	}
}
