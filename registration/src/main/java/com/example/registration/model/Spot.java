package com.example.registration.model;

//import jakarta.persistence.*;

import javax.persistence.*;

@Entity
@Table(name = "parkingspots")

public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;
//    @Column(name = "spot_id")  // Specify the correct column name in the database
//    private Long spotId;  // Corresponds to the spot_id column in the parkingspots table


    private String spotName;
    private String location;
    private String station;
    private int slot;
    private String spotStatus;  // Status of the parking spot (e.g., available, reserved, occupied)

    @Column(name = "price_per_hour")
    private double pricePerHour;
    private int ownerId;        // The ID of the owner (foreign key reference to users table)

    @Column(name = "availability")
    private String availability;

    public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}


//    public Long getId() {
//        return id;
//    }
//
//        public void setId(Long id) {
//        this.id = id;
//    }
    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }


    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getSpotStatus() {
        return spotStatus;
    }

    public void setSpotStatus(String spotStatus) {
        this.spotStatus = spotStatus;
    }
}
