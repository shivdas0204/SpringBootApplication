package com.shivdas.springbootcrud.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivdas.springbootcrud.DTO.Message;
import com.shivdas.springbootcrud.DTO.ProductVO;
import com.shivdas.springbootcrud.dao.ProductRepository;
import com.shivdas.springbootcrud.entity.Product;
import com.shivdas.springbootcrud.exception.ResourceNotFoundException;
import com.shivdas.springbootcrud.request.ProductRequest;
import com.shivdas.springbootcrud.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	@Transactional
	public ProductVO createProduct(ProductRequest productRequest) {

		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.build();

		productRepository.save(product);

		return this.mapProdctToProductVO(product);
	}

	private ProductVO mapProdctToProductVO(Product product) {
		return ProductVO.builder().id(product.getId()).name(product.getName()).description(product.getDescription())
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductVO findProductDetails(Integer id) {
		LOGGER.info("Inside the findProductDetails method");
		
		Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
				Message.builder().code(HttpStatus.NOT_FOUND.ordinal()).detail("Product not found").build()));
		
		return mapProdctToProductVO(product);
	}

}
