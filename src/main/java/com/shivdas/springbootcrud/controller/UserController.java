package com.shivdas.springbootcrud.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivdas.springbootcrud.DTO.UserVO;
import com.shivdas.springbootcrud.request.UserReuqest;
import com.shivdas.springbootcrud.service.UserService;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE},path = "/user")
@Validated
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserVO>> getUserDetails(){
		List<UserVO> userVos= userService.findUserDetailList();
		return new ResponseEntity<List<UserVO>>(userVos,HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<UserVO> saveUserInformation(@Valid @RequestBody UserReuqest userRequest){
		
		UserVO userVo = userService.saveUserInformation(userRequest);
		
		return new ResponseEntity<UserVO>(userVo, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserVO> findUserDetail(@PathVariable final Integer id){
		UserVO userVO = userService.findUserDetails(id);
		return new ResponseEntity<>(userVO, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserVO> updateUserDetails(@Valid @RequestBody UserReuqest userRequest, @PathVariable final Integer id){
		UserVO userVO = userService.updateUserDetails(userRequest,id);
		return new ResponseEntity<>(userVO, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUserDetails(@PathVariable final Integer id){
		String userVO = userService.deleteUserDetails(id);
		return new ResponseEntity<>(userVO, HttpStatus.OK);
	}
}
