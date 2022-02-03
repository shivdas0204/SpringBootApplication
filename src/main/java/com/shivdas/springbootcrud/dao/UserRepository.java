package com.shivdas.springbootcrud.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shivdas.springbootcrud.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("Select u.id from User u where u.email=:email")
	Optional<Integer> findByEmail(@Param("email") final String email);

}
