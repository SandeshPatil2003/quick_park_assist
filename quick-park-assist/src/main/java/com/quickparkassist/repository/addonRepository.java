package com.quickparkassist.repository;

import com.quickparkassist.model.AddonService;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface addonRepository extends JpaRepository<AddonService,Long> {
    
}
