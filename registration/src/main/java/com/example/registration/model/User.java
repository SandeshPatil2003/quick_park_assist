package com.example.registration.model;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String password;
    private String availability;

    private String phoneNumber;
    private String address;

    private String vehicleNumber;
    private String vehicleModel;

    private String role;

    private String hasElectric;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordResetToken passwordResetToken;

    public PasswordResetToken getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public User() {
    }

    // Modified constructor
    public User(String fullName, String email, String password, String availability, String phoneNumber, String address, String vehicleNumber, String vehicleModel, String role,String hasElectric) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.availability = availability;
        this.phoneNumber = phoneNumber;
        this.address = address;  // Now it's a String
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.role = role;  // Now it's a String
        this.hasElectric = hasElectric;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // public String getAvailability() {
    //     return availability;
    // }

    // public void setAvailability(String availability) {
    //     this.availability = availability;
    // }

    public String getRole() {
        return role;  // Returning role as String
    }

    public void setRole(String role) {
        this.role = role;  // Setting role as String
    }

    public String getHasElectric() {
        return hasElectric;
    }

    public void setHasElectric(String hasElectric) {
        this.hasElectric = hasElectric;
    }
}
