
package com.example.quickParkAssist.repository;


import org.springframework.stereotype.Repository;

import com.example.quickParkAssist.model.AddonServiceBooking;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface addonServiceBookingRepository extends JpaRepository<AddonServiceBooking,Long> {
    
}

