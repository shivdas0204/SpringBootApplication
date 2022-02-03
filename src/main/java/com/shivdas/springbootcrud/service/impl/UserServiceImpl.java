package com.shivdas.springbootcrud.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivdas.springbootcrud.DTO.Message;
import com.shivdas.springbootcrud.DTO.UserVO;
import com.shivdas.springbootcrud.dao.UserRepository;
import com.shivdas.springbootcrud.entity.User;
import com.shivdas.springbootcrud.exception.BadRequestException;
import com.shivdas.springbootcrud.exception.ResourceNotFoundException;
import com.shivdas.springbootcrud.request.UserReuqest;
import com.shivdas.springbootcrud.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = false)
	public UserVO saveUserInformation(@Valid UserReuqest userRequest) {
		
		Optional<Integer> optionalId = userRepository.findByEmail(userRequest.getEmail());
		
		if (optionalId.isPresent()) {
			throw new BadRequestException(Message.builder().code(HttpStatus.BAD_REQUEST.ordinal())
					.detail("Email " + userRequest.getEmail() + " Already exists").build());

		}
		
		User user = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
				.email(userRequest.getEmail()).phoneNumber(userRequest.getPhoneNumber()).bithdate(ZonedDateTime.now())
				.build();

		userRepository.save(user);
		UserVO userVO = mapUserToUserVO(user);

		return userVO;
	}

	private UserVO mapUserToUserVO(User user) {
		return UserVO.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail())
				.phoneNumber(user.getPhoneNumber()).build();
	}

	@Override
	@Transactional(readOnly = true)
	public UserVO findUserDetails(Integer id) {
		User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(
				Message.builder().code(HttpStatus.NOT_FOUND.ordinal()).detail("User not found").build()));
		
		return mapUserToUserVO(user);
	}

	@Override
	@Transactional(readOnly = false)
	public UserVO updateUserDetails(@Valid UserReuqest userRequest, Integer id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
				Message.builder().code(HttpStatus.NOT_FOUND.ordinal()).detail("User not found").build()));

		Optional<Integer> optionalId = userRepository.findByEmail(userRequest.getEmail());

		if (optionalId.isPresent() && !optionalId.get().equals(id)) {
			throw new BadRequestException(Message.builder().code(HttpStatus.BAD_REQUEST.ordinal())
					.detail("Email " + userRequest.getEmail() + " Already exists").build());

		}

		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setEmail(userRequest.getEmail());
		user.setPhoneNumber(userRequest.getPhoneNumber());

		userRepository.save(user);
		UserVO userVO = mapUserToUserVO(user);

		return userVO;
	}

	@Override
	public String deleteUserDetails(Integer id) {
		User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(
				Message.builder().code(HttpStatus.NOT_FOUND.ordinal()).detail("User not found").build()));
		userRepository.delete(user);
		return "User deleted successfully";
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserVO> findUserDetailList() {
		List<User> users = userRepository.findAll();
		return users.stream().map(user -> mapUserToUserVO(user)).collect(Collectors.toList());
	}

}
