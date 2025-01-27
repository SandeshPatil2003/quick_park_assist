
package com.quickparkassist.repository;


import org.springframework.stereotype.Repository;

import com.quickparkassist.model.AddonServiceBooking;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface addonServiceBookingRepository extends JpaRepository<AddonServiceBooking,Long> {
    
}

