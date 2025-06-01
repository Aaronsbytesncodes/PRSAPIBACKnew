package com.PRS.DB;


import org.springframework.data.jpa.repository.JpaRepository;

import com.PRS.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

}
