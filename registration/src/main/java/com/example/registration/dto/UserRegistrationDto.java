package com.example.registration.dto;



public class UserRegistrationDto {

    private String fullName;
    private String email;
    private String password;
    private String availability;


    private String phoneNumber;
    private String address;

    private String vehicleNumber;

    private String vehicleModel;
    private String role; // New field for role


    private String hasElectric;




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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    // Default constructor
    public UserRegistrationDto() {
    }

    // Constructor with role field
    public UserRegistrationDto(String fullName, String email, String password, String availability,String phoneNumber,String address,String vehicleNumber,String vehicleModel, String role,String hasElectric) {
        this.fullName = fullName;

        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.availability = availability;
        this.address = address;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.role = role; // Assigning role
        this.hasElectric = hasElectric;
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

    // Getter and Setter methods for the role field
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHasElectric() {
        return hasElectric;
    }

    public void setHasElectric(String hasElectric) {
        this.hasElectric = hasElectric;
    }
}
