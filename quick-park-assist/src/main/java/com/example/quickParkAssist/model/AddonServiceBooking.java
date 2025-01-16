package com.example.quickParkAssist.model;

// import java.util.List;

// import jakarta.persistence.ElementCollection;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_addon_booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddonServiceBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addonServiceBookingID;
    private String serviceIDs;
    private String quantities;
    public String getServiceIDs() {
        return serviceIDs;
    }

    public String getQuantities() {
        return quantities;
    }
    public void setServiceIDs(String serviceIDs) {
        this.serviceIDs = serviceIDs;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }



}
