package com.vims.controller;

import java.util.Calendar;
import java.util.Date;
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

	//===========================================Admin=================================//
	@PostMapping(value = "/admin/login/{username}/{password}")
	public ResponseEntity<?> loginAdmin(@PathVariable("username") String username,@PathVariable("password") String password) {
		
		String cid=custService.findByCustomer(username, password);
		
		if(cid==null) {
			return new ResponseEntity<String>("Customer not found", HttpStatus.OK);

		}
		return new ResponseEntity<String>(" login successful"+cid, HttpStatus.OK);
	}
	
	// ========================================== Customer Controller ===========================================================
	
	@Autowired
	private CustomerService custService;
	List<Customer> customers = null;
	Optional<Customer> customer=null;
	List<VehicleRegistration>policies=null;
	@PostMapping(value = "/customer/login/{username}/{password}")
	public ResponseEntity<?> loginCustomer(@PathVariable("username") String username,@PathVariable("password") String password) {
		
		String cid=custService.findByCustomer(username, password);
		
		if(cid==null) {
			return new ResponseEntity<String>("Customer not found", HttpStatus.OK);

		}
		return new ResponseEntity<String>(" login successful"+cid, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/customer")
	public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
		
		String user=customer.getUsername();
		String pass=customer.getPassword();
		String cid=custService.findByCustomer(user, pass);
		if(cid!=null) {
			return new ResponseEntity<String>("Customer already exists enter different username and password", HttpStatus.OK);
		}
		customer=custService.save(customer);
		
		if(customer==null) {
			return new ResponseEntity<String>("Customer not saved", HttpStatus.OK);

		}
		return new ResponseEntity<String>(customer.getCustomer_id()+" is saved successfully", HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/customer/findall")
	public ResponseEntity<?> listAll() {
		customers = custService.findAll();
		if (customers.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}
	
	@GetMapping("/customer/find/{custId}")
	public ResponseEntity<?> findById(@PathVariable("custId") String custId) throws Exception {
		
		if(custId.equals("P001"))
		throw new Exception();
		
		customer=custService.findById(custId);
		
		if(!customer.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("customer Id  "+custId+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<Customer>> (customer,HttpStatus.OK);
	}
	
	@GetMapping(value = "/customer/findallpolicies/{custId}")
	public ResponseEntity<?> listAllPoliciesOfCustomer(@PathVariable("custId") String custId) {
		policies=custService.getVehicleDetails(custId);
	
		if (policies.isEmpty()) {
			return new ResponseEntity<String>("No vehicle policies available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<VehicleRegistration>>(policies, HttpStatus.OK);
	}
	
	@PutMapping(value = "/customer/update")
	public ResponseEntity<?> updateProduct(@RequestBody Customer customer) {
		customer=custService.save(customer);
		
		if(customer==null) {
			return new ResponseEntity<String>("customer not updated", HttpStatus.OK);

		}
		
		return new ResponseEntity<String>(customer.getCustomer_id()+" is updated", HttpStatus.OK);
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
	
	@PostMapping(value = "/vehicle/{custId}")
	public ResponseEntity<?> saveVehicle(@RequestBody VehicleRegistration vehicle,@PathVariable("custId") String custId) {
		Customer c=custService.findById(custId).get();
		vehicle.setCust(c);
		if(vehicle.getVehicle_class().equalsIgnoreCase("own")){
	        vehicle.setPremium_amount(Double.toString((Double.parseDouble(vehicle.getPrice())*(0.065))/12));
	        
	        }else{
	        	 vehicle.setPremium_amount(Double.toString((Double.parseDouble(vehicle.getPrice())*(0.065)+(Double.parseDouble(vehicle.getPrice()))/12)));
	        	 
	        }
		vehicle=vehicleService.save(vehicle);
		if(vehicle==null) {
			return new ResponseEntity<String>("vehicle not saved", HttpStatus.OK);

		}
		return new ResponseEntity<String>("vehicle with policy id: "+vehicle.getPolicy_id()+" is saved with customer id: "+vehicle.getCust().getCustomer_id()+" premium amount is:"+vehicle.getPremium_amount(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/vehicle/findall")
	public ResponseEntity<?> listAllVehicles() {
		vehicles = vehicleService.findAll();
		if (vehicles.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<VehicleRegistration>>(vehicles, HttpStatus.OK);
	}
	

	@GetMapping("/vehicle/find/{policy_id}")
	public ResponseEntity<?> findVehicleById(@PathVariable("policy_id") String policy_id) throws Exception {
		
		if(policy_id.equals("P001"))
		throw new Exception();
		
		vehicle=vehicleService.findById(policy_id);
		
		if(!vehicle.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("policyd  Id  "+policy_id+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<VehicleRegistration>> (vehicle,HttpStatus.OK);
	}
	
	@PutMapping(value = "/vehicle/update/")
	public ResponseEntity<?> updateVehicle(@RequestBody VehicleRegistration vehicle) {
		vehicle=vehicleService.save(vehicle);
		
		if(vehicle==null) {
			return new ResponseEntity<String>("vehicle not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<String>(vehicle.getPolicy_id()+" is updated", HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/vehicle/delete/{policy_id}")
	public ResponseEntity<?> deleteVehicle(@PathVariable("policy_id") String policy_id) {
		vehicleService.deleteById(policy_id);
		
		return  new ResponseEntity<String>("vehicle  with "+policy_id+" Deleteted", HttpStatus.OK);
	}
	//==========================payment===============================//
	
	@PostMapping(value = "/payment/{policy_id}")
	public ResponseEntity<?> Payment(@PathVariable("policy_id") String policy_id) {
		Date d1=new Date();
		Date max;
		List<DirectPay> l1=vehicleService.getDirectPayDetails(policy_id);
		List<RegisteredPay>l2=vehicleService.getRegisteredPayDetails(policy_id);
		max=l1.get(0).getDue_date();
		for(DirectPay l:l1) {
			if(l.getDue_date().after(max)) {
				max=l.getDue_date();
			}
		}
		for(RegisteredPay l:l2) {
			if(l.getNext_due_date().after(max)) {
				max=l.getNext_due_date();
			}
		}
		if(d1.after(max)) {
			
		}
		return  new ResponseEntity<String>("vehicle  with "+policy_id+" Deleteted", HttpStatus.OK);
	}
	
	
	//========================direct pay============
	@Autowired
	private DirectPayService directPayService;
	List<DirectPay> directpays = null;
	Optional<DirectPay> directpay=null;
	
	@PostMapping(value = "/directpay/{policy_id}")
	public ResponseEntity<?> saveDirectPay(@RequestBody DirectPay pay,@PathVariable("policy_id") String policy_id) {
		VehicleRegistration v=vehicleService.findById(policy_id).get();
		pay.setVehicle(v);
		List<DirectPay> l1=vehicleService.getDirectPayDetails(policy_id);
		Date max;
		max=l1.get(0).getDue_date();
		for(DirectPay l:l1) {
			if(l.getDue_date().after(max)) {
				max=l.getDue_date();
			}
		}
		List<RegisteredPay>l2=vehicleService.getRegisteredPayDetails(policy_id);
		for(RegisteredPay l:l2) {
			if(l.getNext_due_date().after(max)) {
				max=l.getNext_due_date();
			}
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(max);
		cal.add(Calendar.MONTH, 1);
		pay.setDue_date(cal.getTime());
		String pa=v.getPremium_amount();
		double fap=0;
		 if(pay.getPay_mode().toLowerCase()=="creditcard"){
		        fap=Long.parseLong(pa)*0.023;
		        }
		        else{
		        	 fap=Long.parseLong(pa);
		        }
		        //double fap1=String.valueOf(fap);
		        pay.setAmount_paid(fap);
		pay=directPayService.save(pay);
		
		if(directpay==null) {
			return new ResponseEntity<String>("directpay not saved", HttpStatus.OK);

		}
		return new ResponseEntity<String>("payment processed successfully and payment id is:"+pay.getPayment_id()+"next payment date is"+pay.getDue_date(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/directpay/findall")
	public ResponseEntity<?> listAllDirectPay() {
		directpays = directPayService.findAll();
		if (directpays.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<DirectPay>>(directpays, HttpStatus.OK);
	}
	
	@GetMapping("/directpay/{payment_id}")
	public ResponseEntity<?> findDirectPayById(@PathVariable("payment_id") String payment_id) throws Exception {
		
		if(payment_id.equals("P001"))
		throw new Exception();
		
		directpay=directPayService.findById(payment_id);
		
		if(!directpay.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("payment_id    "+payment_id+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<DirectPay>> (directpay,HttpStatus.OK);
	}
	
	@PutMapping(value = "/directpay/update/")
	public ResponseEntity<?> updateDirectPay(@RequestBody DirectPay directpay) {
		directpay=directPayService.save(directpay);
		
		if(directpay==null) {
			return new ResponseEntity<String>("directpay not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<DirectPay>(directpay, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/directpay/delete/{payment_id}")
	public ResponseEntity<?> deleteDirectPay(@PathVariable("payment_id") String payment_id) {
		directPayService.deleteById(payment_id);
		
		return  new ResponseEntity<String>("directpay Id with "+payment_id+" Deleteted", HttpStatus.OK);
	}
	
	//=============================registered pay=========================//
	@Autowired
	private RegisteredPayService registeredPayService;
	List<RegisteredPay> registeredpays = null;
	Optional<RegisteredPay> registeredpay=null;
	
	@PostMapping(value = "/registeredpay/{policy_id}")
	public ResponseEntity<?> saveRegisteredPay(@RequestBody RegisteredPay pay,@PathVariable("policy_id") String policy_id) {
		VehicleRegistration v=vehicleService.findById(policy_id).get();
		pay.setVehicle(v);
		
		List<DirectPay> list1=vehicleService.getDirectPayDetails(policy_id);
		Date max;
		max=list1.get(0).getDue_date();
		for(DirectPay l:list1) {
			if(l.getDue_date().after(max)) {
				max=l.getDue_date();
			}
		}
		List<RegisteredPay>list2=vehicleService.getRegisteredPayDetails(policy_id);
		
		for(RegisteredPay l:list2) {
			if(l.getNext_due_date().after(max)) {
				max=l.getNext_due_date();
			}
			
		}
		Calendar cal1=Calendar.getInstance();
		cal1.setTime(max);
		pay.setDue_date(max);
		cal1.add(Calendar.MONTH, 1);
		pay.setNext_due_date(cal1.getTime());
		
		String pa=v.getPremium_amount();
		 double fap=0;
    	 Date d1=pay.getDue_date();
    		        Date d2=pay.getPaid_date();
    		        Calendar cal=Calendar.getInstance();
    		        cal.setTime(d1);long l1=cal.getTimeInMillis();
    		        cal.setTime(d2);long l2=cal.getTimeInMillis();
    		        long diff=(l2-l1)/(60*60*24*1000); 
    		        if(pay.getPayment_mode().toLowerCase()=="creditcard"){
    		        fap=Long.parseLong(pa)*diff*0.0056*0.023;
    		        }
    		        else{
    		        	 fap=Long.parseLong(pa)*diff*0.0056;
    		        }
    		        String fap1=String.valueOf(fap);
    	 pay.setPay_amount(fap1);
    	
    	 pay=registeredPayService.save(pay);
		
		if(registeredpay==null) {
			return new ResponseEntity<String>("registeredpay not saved", HttpStatus.OK);

		}
		return new ResponseEntity<String>("payment processed successfully with payment id: "+pay.getPayment_id()+"and next payment date is: "+pay.getNext_due_date(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/registeredpay/findall")
	public ResponseEntity<?> listAllRegisteredPay() {
		registeredpays = registeredPayService.findAll();
		if (registeredpays.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<RegisteredPay>>(registeredpays, HttpStatus.OK);
	}
	
	@GetMapping("/registeredpay/{payment_id}")
	public ResponseEntity<?> findRegisteredPayById(@PathVariable("payment_id") String payment_id) throws Exception {
		
		if(payment_id.equals("P001"))
		throw new Exception();
		
		registeredpay=registeredPayService.findById(payment_id);
		
		if(!registeredpay.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("payment_id    "+payment_id+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<RegisteredPay>> (registeredpay,HttpStatus.OK);
	}
	
	@PutMapping(value = "/registeredpay/update/")
	public ResponseEntity<?> updateRegisteredPay(@RequestBody RegisteredPay registeredpay) {
		registeredpay=registeredPayService.save(registeredpay);
		
		if(registeredpay==null) {
			return new ResponseEntity<String>("registeredpay not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<RegisteredPay>(registeredpay, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/registeredpay/delete/{payment_id}")
	public ResponseEntity<?> deleteRegisteredPay(@PathVariable("payment_id") String payment_id) {
		registeredPayService.deleteById(payment_id);
		
		return  new ResponseEntity<String>("registeredpay Id with "+payment_id+" Deleteted", HttpStatus.OK);
	}
	
	
	//==================================accident claim=============================//
	@Autowired
	private AccidentClaimService accidentclaimservice;
	List<AccidentClaim> accidentclaims = null;
	Optional<AccidentClaim> accidentclaim=null;
	
	@PostMapping(value = "/accidentclaim/{custId}/{policy_id}")
	public ResponseEntity<?> saveAccidentClaim(@RequestBody AccidentClaim accidentclaim,@PathVariable("custId") String custId,@PathVariable("policy_id") String policy_id) {
		Customer c=custService.findById(custId).get();
		VehicleRegistration v=vehicleService.findById(policy_id).get();
		accidentclaim.setCust(c);
		accidentclaim.setVehicle(v);
		String type=accidentclaim.getAccident_type();
		if(v.getVehicle_type()=="TW") {

        if(type.equalsIgnoreCase("fire")){
        	accidentclaim.setWeightage("80");
        }else if(type.equalsIgnoreCase("road accident")){
        	accidentclaim.setWeightage("70");
        }
        else if(type.equalsIgnoreCase("malicious acts")){
        	accidentclaim.setWeightage("50");
        }else if(type.equalsIgnoreCase("self-destruction")){
        	accidentclaim.setWeightage("40");
        }else if(type.equalsIgnoreCase("part failure")){
        	accidentclaim.setWeightage("20");
        }
		}
		if(v.getVehicle_type()=="FW") {
			 if(type.equalsIgnoreCase("fire")){
		        	accidentclaim.setWeightage("70");
		        }else if(type.equalsIgnoreCase("road accident")){
		        	accidentclaim.setWeightage("65");
		        }
		        else if(type.equalsIgnoreCase("malicious acts")){
		        	accidentclaim.setWeightage("50");
		        }else if(type.equalsIgnoreCase("self-destruction")){
		        	accidentclaim.setWeightage("30");
		        }else if(type.equalsIgnoreCase("part failure")){
		        	accidentclaim.setWeightage("20");
		        }
		}
        List<AccidentClaim>aclaims=vehicleService.getAccidentClaimDetails(policy_id);
        List<TheftClaim>tclaims=vehicleService.getTheftClaimDetails(policy_id);
        if(aclaims.size()>0||tclaims.size()>0) {
        	return new ResponseEntity<String>("not eligible for claim as you have already claimed", HttpStatus.OK);
        }
        accidentclaim.setClaim_amount((Double.toString((Double.parseDouble(accidentclaim.getTotal_amount())*Double.parseDouble(accidentclaim.getWeightage())/100))));
		double amount=Double.parseDouble(accidentclaim.getTotal_amount())*Double.parseDouble(accidentclaim.getWeightage())/100;
		if(amount<5000&&v.getVehicle_type()=="TW") {
			return new ResponseEntity<String>("not eligible for claim as claim amount is less than 5000", HttpStatus.OK);
		}
		else if(amount<20000&&v.getVehicle_type()=="FW") {
			return new ResponseEntity<String>("not eligible for claim as claim amount is less than 20000", HttpStatus.OK);
		}
		
		accidentclaim=accidentclaimservice.save(accidentclaim);
		
		if(accidentclaim==null) {
			return new ResponseEntity<String>("accidentclaim not saved", HttpStatus.OK);

		}
		return new ResponseEntity<String>("claimed processed sucessfully and claim id is :"+accidentclaim.getClaim_id()+" and claimed amount is:"+accidentclaim.getClaim_amount(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/accidentclaim/findall")
	public ResponseEntity<?> listAllAccidentClaim() {
		accidentclaims = accidentclaimservice.findAll();
		if (accidentclaims.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<AccidentClaim>>(accidentclaims, HttpStatus.OK);
	}
	
	@GetMapping("/accidentclaim/{claim_id}")
	public ResponseEntity<?> findAccidentClaimById(@PathVariable("claim_id") String claim_id) throws Exception {
		
		if(claim_id.equals("P001"))
		throw new Exception();
		
		accidentclaim=accidentclaimservice.findById(claim_id);
		
		if(!accidentclaim.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("claim_id   "+claim_id+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<AccidentClaim>> (accidentclaim,HttpStatus.OK);
	}
	
	@PutMapping(value = "/accidentclaim/update/")
	public ResponseEntity<?> updateAccidentClaim(@RequestBody AccidentClaim accidentclaim) {
		accidentclaim=accidentclaimservice.save(accidentclaim);
		
		if(accidentclaim==null) {
			return new ResponseEntity<String>("accidentclaim not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<AccidentClaim>(accidentclaim, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/accidentclaim/delete/{claim_id}")
	public ResponseEntity<?> deleteAccidentClaim(@PathVariable("claim_id") String claim_id) {
		accidentclaimservice.deleteById(claim_id);
		
		return  new ResponseEntity<String>("accidentclaim Id with "+claim_id+" Deleteted", HttpStatus.OK);
	}
	
	//========================theft claim=================//
	@Autowired
	private TheftClaimService theftclaimservice;
	List<TheftClaim> theftclaims = null;
	Optional<TheftClaim> theftclaim=null;
	
	@PostMapping(value = "/theftclaim/{custId}/{policy_id}")
	public ResponseEntity<?> saveTheftClaim(@RequestBody TheftClaim theftclaim,@PathVariable("custId") String custId,@PathVariable("policy_id") String policy_id) {
		Customer c=custService.findById(custId).get();
		VehicleRegistration v=vehicleService.findById(policy_id).get();
		theftclaim.setCust(c);
		theftclaim.setVehicle(v);
		theftclaim.setClaim_amount(Double.toString((Double.parseDouble(theftclaim.getTotal_amount())*0.75)));
		Date d1=theftclaim.getTheft_date();
        Date d2=theftclaim.getComplaint_date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(d1);long l1=cal.getTimeInMillis();
        cal.setTime(d2);long l2=cal.getTimeInMillis();
        long diff=(l2-l1)/(60*60*24*1000);
        List<AccidentClaim>aclaims=vehicleService.getAccidentClaimDetails(policy_id);
        List<TheftClaim>tclaims=vehicleService.getTheftClaimDetails(policy_id);
        if(aclaims.size()>0||tclaims.size()>0) {
        	return new ResponseEntity<String>("not eligible for claim as you have already claimed", HttpStatus.OK);
        }
        if(diff>10){
        	return new ResponseEntity<String>("not eligible for claim as you have filed complaint after 10days of theft", HttpStatus.OK);
        }
        
		theftclaim=theftclaimservice.save(theftclaim);
		
		if(theftclaim==null) {
			return new ResponseEntity<String>("theftclaim not saved", HttpStatus.OK);

		}
		return new ResponseEntity<String>("claimed processed successfully and claim id is: "+theftclaim.getClaim_id(), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/theftclaim/findall")
	public ResponseEntity<?> listAllTheftClaim() {
		theftclaims = theftclaimservice.findAll();
		if (theftclaims.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<TheftClaim>>(theftclaims, HttpStatus.OK);
	}
	
	@GetMapping("/theftclaim/{claim_id}")
	public ResponseEntity<?> findTheftClaimById(@PathVariable("claim_id") String claim_id) throws Exception {
		
		if(claim_id.equals("P001"))
		throw new Exception();
		
		theftclaim=theftclaimservice.findById(claim_id);
		
		if(!theftclaim.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("claim_id  Id  "+claim_id+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<TheftClaim>> (theftclaim,HttpStatus.OK);
	}
	
	@PutMapping(value = "/theftclaim/update/")
	public ResponseEntity<?> updateTheftClaim(@RequestBody TheftClaim theftclaim) {
		theftclaim=theftclaimservice.save(theftclaim);
		
		if(theftclaim==null) {
			return new ResponseEntity<String>("theftclaim not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<TheftClaim>(theftclaim, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/theftclaim/delete/{claim_id}")
	public ResponseEntity<?> deleteTheftClaim(@PathVariable("claim_id") String claim_id) {
		theftclaimservice.deleteById(claim_id);
		
		return  new ResponseEntity<String>("theftclaim Id with "+claim_id+" Deleteted", HttpStatus.OK);
	}
	
	
	//==============================cancellation======================//
	@Autowired
	private CancellationService cancelservice;
	List<Cancellation> cancels = null;
	Optional<Cancellation> cancel=null;
	
	@PostMapping(value = "/cancel/{custId}/{policy_id}")
	public ResponseEntity<?> saveCancellation(@RequestBody Cancellation can,@PathVariable("custId") String custId,@PathVariable("policy_id") String policy_id) {
		Customer c=custService.findById(custId).get();
		VehicleRegistration v=vehicleService.findById(policy_id).get();
		can.setCust(c);
		can.setVehicle(v);
		can.setRegistered_date(v.getDate_of_purchase());
		can.setTotal_amount(Double.toString(directPayService.findTotalAmountByCustomerId(custId)));

	    Date s2=can.getCancel_date();
	       Date s1=can.getLast_paid_date();
	
	       Calendar cal=Calendar.getInstance();
	       cal.setTime(s1);
	       int months1=cal.get(Calendar.MONTH);
	       int year1=cal.get(Calendar.YEAR);
	       cal.setTime(s2);
	       int months2=cal.get(Calendar.MONTH);
	       int year2=cal.get(Calendar.YEAR);
	       int n=((year2-year1)*12)+(months2-months1);
	       
	       Date s4=can.getCancel_date();
	       Date s3=can.getRegistered_date();
	       Calendar cal1=Calendar.getInstance();
	       cal1.setTime(s3);
	       int mont1=cal1.get(Calendar.MONTH);
	       int yea1=cal1.get(Calendar.YEAR);
	       cal1.setTime(s4);
	       int mont2=cal1.get(Calendar.MONTH);
	       int yea2=cal1.get(Calendar.YEAR);
	       int m=((yea2-yea1)*12)+(mont2-mont1);
	       
	       if(n>3){
	    	   if(n>9){
	    		   String s=can.getTotal_amount();
	    		   long l= Long.parseLong(s);
	    		   double t=0;
	    		   String to=String.valueOf(t);
	    		   can.setWithdraw_amount(to);
	    	   }
	    	   else if(n==9){
	    		   String s=can.getTotal_amount();
	    		   long l= Long.parseLong(s);
	    		   double t=l-(l*0.1235);
	    		   String to=String.valueOf(t);
	    		   can.setWithdraw_amount(to);
	    		   
	    	   }
	    	   else if(n>=7){
	    		   String s=can.getTotal_amount();
	    		   long l= Long.parseLong(s);
	    		   double t=l-(l*0.1025);
	    		   String to=String.valueOf(t);
	    		   can.setWithdraw_amount(to);
	    		   
	    	   }
	    	   
	    	      else if(n>=5){
	    		   String s=can.getTotal_amount();
	    		   long l= Long.parseLong(s);
	    		   double t=l-(l*0.07);
	    		   String to=String.valueOf(t);
	    		   can.setWithdraw_amount(to);
	    	   }
	    	   
	    	  
	       }
	       else{
	    	   if(m>3){
	    		   String s=can.getTotal_amount();
	    		   long l= Long.parseLong(s);
	    		   double t=l-(l*0.1235);
	    		   String to=String.valueOf(t);
	    		   can.setWithdraw_amount(to);
	    	   }
	    	   else{
	    		   double t=0;
	    		   String to=String.valueOf(t);
	    		   can.setWithdraw_amount(to);
	    	   }
	       }
	    
	    can=cancelservice.save(can);
		
		if(cancel==null) {
			return new ResponseEntity<String>("cancel not saved", HttpStatus.OK);

	}
		return new ResponseEntity<Cancellation>(can, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/cancel/findall")
	public ResponseEntity<?> listAllCancellation() {
		cancels = cancelservice.findAll();
		if (cancels.isEmpty()) {
			return new ResponseEntity<String>("No Records available in DB", HttpStatus.OK);
		}

		return new ResponseEntity<List<Cancellation>>(cancels, HttpStatus.OK);
	}

	
	@GetMapping("/cancel/{cancel_id}")
	public ResponseEntity<?> findCancellationById(@PathVariable("cancel_id") String cancel_id) throws Exception {
		
		if(cancel_id.equals("P001"))
		throw new Exception();
		
		cancel=cancelservice.findById(cancel_id);
		
		if(!cancel.isPresent()) {
			//System.out.println("---- null");
			return new ResponseEntity<String>("cancel  Id  "+cancel_id+"  not found",HttpStatus.OK);
		}
		
		return new ResponseEntity<Optional<Cancellation>> (cancel,HttpStatus.OK);
	}
	
	@PutMapping(value = "/cancel/update/")
	public ResponseEntity<?> updateCancellation(@RequestBody Cancellation cancel) {
		cancel=cancelservice.save(cancel);
		
		if(cancel==null) {
			return new ResponseEntity<String>("cancel not saved", HttpStatus.OK);

		}
		
		return new ResponseEntity<Cancellation>(cancel, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/cancel/delete/{cancel_id}")
	public ResponseEntity<?> deleteCancellation(@PathVariable("cancel_id") String cancel_id) {
		cancelservice.deleteById(cancel_id);
		
		return  new ResponseEntity<String>("cancel Id with "+cancel_id+" Deleteted", HttpStatus.OK);
	}
	
	}
