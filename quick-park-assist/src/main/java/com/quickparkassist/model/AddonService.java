package com.quickparkassist.model;

//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
import lombok.*;


import javax.persistence.*;
@Entity
@Table(name = "tbl_addon")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddonService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ServiceID;
    private String name;
    private String description;
    private float price;
    private float duration;
    private boolean isActive;
    // Custom getter for compatibility
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Long getServiceID() {
        return ServiceID;
    }

}
