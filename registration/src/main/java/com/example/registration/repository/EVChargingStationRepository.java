package com.example.registration.repository;



import com.example.registration.model.EVChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Ensure this is imported

public interface EVChargingStationRepository extends JpaRepository<EVChargingStation, Long> {
    // Additional custom queries can be defined here if needed
//    List<EVChargingStation> findAll();
}
