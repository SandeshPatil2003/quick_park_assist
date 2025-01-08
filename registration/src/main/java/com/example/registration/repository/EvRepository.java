package com.example.registration.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.registration.model.Evmodel;

public interface EvRepository extends JpaRepository<Evmodel, Long> {
}
