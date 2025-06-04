package com.PRS.DB;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PRS.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByUsername(Object username);

}
