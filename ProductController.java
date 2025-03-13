package com.rest.controller;

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

import com.rest.model.Product;
import com.rest.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	ProductService productService;
	
	//implement the logic for getting all products
	@GetMapping("/listOfProducts")
	
	public ResponseEntity<List<Product>> getAllProducts()
	{
	  List<Product> products =  productService.getAllProducts();
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
	//Implements the logic for getting a specfific product
	@GetMapping({"/getById/{id}"})
	public ResponseEntity<Product> getProduct(@PathVariable Integer id)
	{
		return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
		
	}
	
	//IMPLEMET THE LOGIC FOR INSERT PRODUCT DATA
	@PostMapping("/addProduct")
	public ResponseEntity<Product> saveProduct(@RequestBody Product product)
	{
		Product p = productService.insert(product);
		HttpHeaders headers = new HttpHeaders();
		headers.add("product", "/api/product/addProduct/"+p.getId());
		return new ResponseEntity<>(p,headers,HttpStatus.OK);
		
	}
	
	//update logic
	@PutMapping({"/updateProduct/{id}"})
	public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id , @RequestBody Product product)
	{
		productService.updateProduct(id, product);
		return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
		
	}
	
	//delete logic
	@DeleteMapping({"/deleteProduct/{id}"})
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") Integer id)
	{
		productService.deleteProduct(id);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
}
