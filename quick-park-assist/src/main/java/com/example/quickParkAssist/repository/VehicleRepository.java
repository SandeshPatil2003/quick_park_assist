package com.example.quickParkAssist.repository;

import com.example.quickParkAssist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.quickParkAssist.model.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    // Custom queries can be added here if necessary
    List<Vehicle> findByUser(User user);
    List<Vehicle> findByUserEmail(String email);
    List<Vehicle> findByUserIdAndHasElectric(Long userId, String hasElectric);
    List<Vehicle> findByUserAndHasElectric(User user, String hasElectric);
    List<Vehicle> findByUserEmailAndHasElectric(String email, String hasElectric);
}
