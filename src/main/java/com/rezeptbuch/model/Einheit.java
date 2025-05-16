package com.rezeptbuch.model;
import jakarta.persistence.*;

@Entity
public class Einheit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long einheitID;

    private String name;

    // Getter und Setter

    public Long getEinheitID() {
        return einheitID;
    }

    public void setEinheitID(Long einheitID) {
        this.einheitID = einheitID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

