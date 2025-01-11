package com.example.registration.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.registration.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	Optional<User> findIdByEmail(String email);  // Return Optional<User> to use map
//	Optional<User> findByUsername(String username);
	//User findByEmail(String email);
Optional<User> findById(Long userId);

}
