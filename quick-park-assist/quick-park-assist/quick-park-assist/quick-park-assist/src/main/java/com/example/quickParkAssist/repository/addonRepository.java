package com.example.quickParkAssist.repository;

import com.example.quickParkAssist.model.AddonService;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface addonRepository extends JpaRepository<AddonService,Long> {
    
}
