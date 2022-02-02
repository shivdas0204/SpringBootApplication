package com.shivdas.springbootcrud.service;

import com.shivdas.springbootcrud.DTO.ProductVO;
import com.shivdas.springbootcrud.request.ProductRequest;

public interface ProductService {

	ProductVO createProduct(ProductRequest productRequest);

	ProductVO findProductDetails(Integer id);

}
