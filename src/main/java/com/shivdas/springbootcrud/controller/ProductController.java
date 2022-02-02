package com.shivdas.springbootcrud.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shivdas.springbootcrud.DTO.ProductVO;
import com.shivdas.springbootcrud.request.ProductRequest;
import com.shivdas.springbootcrud.service.ProductService;

@RestController
@Validated
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/product")
	public ResponseEntity<ProductVO> createProduct(@Valid @RequestBody ProductRequest productRequest){
		ProductVO productVO = productService.createProduct(productRequest);
		return new ResponseEntity<ProductVO>(productVO, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductVO> getProductDetails(@PathVariable Integer id){
		ProductVO productVO = productService.findProductDetails(id);
		return new ResponseEntity<ProductVO>(productVO, HttpStatus.OK);
	}
}
