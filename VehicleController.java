package com.car.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.model.Vehicle;
import com.car.service.VehicleServices;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
	
	
	@Autowired
	VehicleServices vs;
	//implement the logic for getting all vehicles
	@GetMapping("/listofvehicles")
	public ResponseEntity<List<Vehicle>> getallVehicle(){
		List<Vehicle> vehicle = vs.getallVehicle();
		return new ResponseEntity<>(vehicle,HttpStatus.OK);
	}
	//implement the logic for getting  filter by Brand
	@GetMapping("/listofvehicles-by-brand/{brand}")
	public ResponseEntity<List<Vehicle>> getVehiclesbyBrand(@PathVariable String brand){
		List<Vehicle> vehicle = vs.getVehiclesbyBrand(brand);
		if (vehicle.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if no vehicles found
        }
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }
	
	
	//implement the logic for getting  filter by Year

	@GetMapping("/listofvehicles-by-year/{year}")
	public ResponseEntity<List<Vehicle>> getVehiclesbyYear(@PathVariable int year){
		List<Vehicle> vehicle = vs.getVehiclesbyYear(year);
		if (vehicle.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if no vehicles found
        }
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }
	    
	
	//implement the logic for getting  Sort by year
	@GetMapping("/listofvehicles/sort-by-year")
	public ResponseEntity<List<Vehicle>> sortVechiclesByYear(){
		List<Vehicle> vehicle = vs.sortVechiclesByYear();
		return new ResponseEntity<>(vehicle,HttpStatus.OK);
	}
	
	//implement the logic for getting  Sort by price

	@GetMapping("/listofvehicles/sort-by-price")
	public ResponseEntity<List<Vehicle>> sortVechiclesByPrice(){
		List<Vehicle> vehicle = vs.sortVechiclesByPrice();
		return new ResponseEntity<>(vehicle,HttpStatus.OK);
	}
	
	//IMPLEMET THE LOGIC FOR INSERT PRODUCT DATA
		@PostMapping("/addVehicle")
		public ResponseEntity<Vehicle> saveVehical(@RequestBody Vehicle vehical)
		{
			Vehicle v = vs.insert(vehical);
			HttpHeaders headers = new HttpHeaders();
			headers.add("vehical", "/api/vehical/addProduct/"+v.getId());
			return new ResponseEntity<>(v ,headers,HttpStatus.OK);
			
		}
		
		@PutMapping({"/updateVehicle/{id}"})
		public ResponseEntity<Vehicle> updateProduct(@PathVariable("id") Integer id , @RequestBody Vehicle vehicle)
		{
			vs.updateVehicle(id, vehicle);
			return new ResponseEntity<>(vs.getVehicleById(id),HttpStatus.OK);
			
		}
		
		@DeleteMapping({"/deleteProduct/{id}"})
		public ResponseEntity<Vehicle> deleteVehicle(@PathVariable("id") Integer id)
		{
			vs.deleteVehicle(id);
			return new ResponseEntity<>(HttpStatus.OK);
			
		}

}
