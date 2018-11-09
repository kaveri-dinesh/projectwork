package com.vims.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vims.model.AccidentClaim;
import com.vims.model.Cancellation;
import com.vims.model.Customer;
import com.vims.model.DirectPay;
import com.vims.model.RegisteredPay;
import com.vims.model.TheftClaim;
import com.vims.model.VehicleRegistration;
import com.vims.service.AccidentClaimService;
import com.vims.service.CancellationService;
//import com.vims.service.CancellationService;
import com.vims.service.CustomerService;
import com.vims.service.DirectPayService;
import com.vims.service.RegisteredPayService;
import com.vims.service.TheftClaimService;
import com.vims.service.VehicleService;

@RestController
@CrossOrigin
@ComponentScans(value = { @ComponentScan("com.vims.dao"), @ComponentScan("com.vims.service") })
@RequestMapping(value = "/vims")
public class Controller {

	// ========================================== Customer Controller ===========================================================
	
	@Autowired
	private CustomerService custService;
	List<Customer> customers = null;
	Optional<Customer> customer=null;
	
	@PostMapping(value = "/customer")
	public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
		customer=custService.save(customer);
		
		if(customer==null) {
			return new ResponseEntity<String>("Customer not saved", HttpStatus.OK);

		}
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/customer/")
	public ResponseEntity<?> listAll() {
		customers = custService.findAll();
		if (customers.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}
	
	@GetMapping("/customer/{custId}")
	public ResponseEntity<?> findById(@PathVariable("custId") String custId) throws Exception {
		
		if(custId.equals("P001"))
		throw new Exception();
		
		customer=custService.findById(custId);
		
		if(!customer.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("customer Id  "+custId+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<Customer>> (HttpStatus.OK);
	}
	
	@PutMapping(value = "/customer/update/")
	public ResponseEntity<?> updateProduct(@RequestBody Customer customer) {
		customer=custService.save(customer);
		
		if(customer==null) {
			return new ResponseEntity<String>("customer not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/customer/delete/{custId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("custId") String custId) {
		custService.deleteById(custId);
		
		return  new ResponseEntity<String>("Product Id with "+custId+" Deleteted", HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleExceptin(Exception e) {
		return new ResponseEntity<String>("Provided URL not valid. make sure its should be /customers/ "+e,HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/error")
	public ResponseEntity<String> handleExceptin1() {
		return new ResponseEntity<String>("Provided URL not valid. make sure its should be /customers/ ",HttpStatus.NOT_FOUND);
	}
	
	
	
	// ==========================================================================================================================
	
	// ======================================vehicle registration===============================//
	
	@Autowired
	private VehicleService vehicleService;
	List<VehicleRegistration> vehicles = null;
	Optional<VehicleRegistration> vehicle=null;
	
	@PostMapping(value = "/vehicle")
	public ResponseEntity<?> saveVehicle(@RequestBody VehicleRegistration vehicle) {
		vehicle=vehicleService.save(vehicle);
		
		if(vehicle==null) {
			return new ResponseEntity<String>("vehicle not saved", HttpStatus.OK);

		}
		return new ResponseEntity<VehicleRegistration>(vehicle, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/vehicle/")
	public ResponseEntity<?> listAllVehicles() {
		vehicles = vehicleService.findAll();
		if (customers.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}
	//========================direct pay============
	@Autowired
	private DirectPayService directPayService;
	List<DirectPay> directpays = null;
	Optional<DirectPay> directpay=null;
	
	@PostMapping(value = "/directpay")
	public ResponseEntity<?> saveDirectPay(@RequestBody DirectPay directpay) {
		directpay=directPayService.save(directpay);
		
		if(directpay==null) {
			return new ResponseEntity<String>("directpay not saved", HttpStatus.OK);

		}
		return new ResponseEntity<DirectPay>(directpay, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/directpay/")
	public ResponseEntity<?> listAllDirectPay() {
		directpays = directPayService.findAll();
		if (directpays.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<DirectPay>>(directpays, HttpStatus.OK);
	}
	
	//=============================registered pay=========================//
	@Autowired
	private RegisteredPayService registeredPayService;
	List<RegisteredPay> registeredpays = null;
	Optional<RegisteredPay> registeredpay=null;
	
	@PostMapping(value = "/registeredpay")
	public ResponseEntity<?> saveRegisteredPay(@RequestBody RegisteredPay registeredpay) {
		registeredpay=registeredPayService.save(registeredpay);
		
		if(registeredpay==null) {
			return new ResponseEntity<String>("registeredpay not saved", HttpStatus.OK);

		}
		return new ResponseEntity<RegisteredPay>(registeredpay, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/registeredpay/")
	public ResponseEntity<?> listAllRegisteredPay() {
		registeredpays = registeredPayService.findAll();
		if (registeredpays.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<RegisteredPay>>(registeredpays, HttpStatus.OK);
	}
	//==================================accident claim=============================//
	@Autowired
	private AccidentClaimService accidentclaimservice;
	List<AccidentClaim> accidentclaims = null;
	Optional<AccidentClaim> accidentclaim=null;
	
	@PostMapping(value = "/accidentclaim")
	public ResponseEntity<?> saveAccidentClaim(@RequestBody AccidentClaim accidentclaim) {
		accidentclaim=accidentclaimservice.save(accidentclaim);
		
		if(accidentclaim==null) {
			return new ResponseEntity<String>("accidentclaim not saved", HttpStatus.OK);

		}
		return new ResponseEntity<AccidentClaim>(accidentclaim, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/accidentclaim/")
	public ResponseEntity<?> listAllAccidentClaim() {
		accidentclaims = accidentclaimservice.findAll();
		if (accidentclaims.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<AccidentClaim>>(accidentclaims, HttpStatus.OK);
	}
	//========================theft claim=================//
	@Autowired
	private TheftClaimService theftclaimservice;
	List<TheftClaim> theftclaims = null;
	Optional<TheftClaim> theftclaim=null;
	
	@PostMapping(value = "/theftclaim")
	public ResponseEntity<?> saveTheftClaim(@RequestBody TheftClaim theftclaim) {
		theftclaim=theftclaimservice.save(theftclaim);
		
		if(theftclaim==null) {
			return new ResponseEntity<String>("theftclaim not saved", HttpStatus.OK);

		}
		return new ResponseEntity<TheftClaim>(theftclaim, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/theftclaim/")
	public ResponseEntity<?> listAllTheftClaim() {
		theftclaims = theftclaimservice.findAll();
		if (theftclaims.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<TheftClaim>>(theftclaims, HttpStatus.OK);
	}
	//==============================cancellation======================//
	@Autowired
	private CancellationService cancelservice;
	List<Cancellation> cancels = null;
	Optional<Cancellation> cancel=null;
	
	@PostMapping(value = "/cancel")
	public ResponseEntity<?> saveCancellation(@RequestBody Cancellation cancel) {
		cancel=cancelservice.save(cancel);
		
		if(cancel==null) {
			return new ResponseEntity<String>("cancel not saved", HttpStatus.OK);

	}
		return new ResponseEntity<Cancellation>(cancel, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/cancel/")
	public ResponseEntity<?> listAllCancellation() {
		cancels = cancelservice.findAll();
		if (cancels.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<Cancellation>>(cancels, HttpStatus.OK);
	}

	}
