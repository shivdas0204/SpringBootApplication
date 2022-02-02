package com.shivdas.springbootcrud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivdas.springbootcrud.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
