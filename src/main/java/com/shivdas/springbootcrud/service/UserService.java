package com.shivdas.springbootcrud.service;

import java.util.List;

import javax.validation.Valid;

import com.shivdas.springbootcrud.DTO.UserVO;
import com.shivdas.springbootcrud.request.UserReuqest;

public interface UserService {

	UserVO saveUserInformation(@Valid UserReuqest userRequest);

	UserVO findUserDetails(Integer id);

	UserVO updateUserDetails(@Valid UserReuqest userRequest, Integer id);

	String deleteUserDetails(Integer id);

	List<UserVO> findUserDetailList();

}
