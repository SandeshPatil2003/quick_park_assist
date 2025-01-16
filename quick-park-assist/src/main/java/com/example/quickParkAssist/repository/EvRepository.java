package com.example.quickParkAssist.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quickParkAssist.model.Evmodel;

public interface EvRepository extends JpaRepository<Evmodel, Long> {
}
