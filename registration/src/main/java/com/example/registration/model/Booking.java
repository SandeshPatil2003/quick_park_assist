package com.example.registration.model;


//import jakarta.persistence.*;

//import org.apache.catalina.User;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")

public class Booking {

    public enum Status {
        CONFIRMED,
        PENDING,
        COMPLETED,
        CANCELLED
    }

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;


    private String fullName;
    private String duration;


    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    private String price;
    private String paymentMethod;
    private String mobileNumber;
    private Long parkingSpotId; // Reference to the selected parking spot ID


    @Transient // This field is not persisted in the Booking table
    private String username;

    // Add a property for userId
    //private Long userId;
    @Column(name = "user_id") // Maps to "user_id" column in the database
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "spot_Id")
    private Spot spot;

@Enumerated(EnumType.STRING)
private Status status;

    // Here, we store the location directly in the parking_spot column
    @Column(name = "parking_spot")  // Use the parking_spot column to store the location
    private String location;  //  Store location description as string (e.g., Downtown, 5th Avenue)



    // Getters and setters...
    public Status getStatus() {
        return status;
    }

    // Set the status of the booking
    public void setStatus(Status status) {
        this.status = status;
    }
    public Long getId() {
        return bookingId;
    }

    public void setId(Long bookingId) {
        this.bookingId = bookingId;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

