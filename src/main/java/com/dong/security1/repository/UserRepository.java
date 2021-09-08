package com.dong.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dong.security1.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByUsername(String username);

}
