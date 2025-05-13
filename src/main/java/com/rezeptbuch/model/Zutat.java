//Lasse Helmig
package com.rezeptbuch.model;
import jakarta.persistence.*;

@Entity
public class Zutat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zutatID;

    private String name;

    private Double kcalPro100g;

    @ManyToOne
    @JoinColumn(name = "einheit_id")
    //private Einheit einheit;

    // Getter und Setter

    public Long getZutatID() {
        return zutatID;
    }

    public void setZutatID(Long zutatID) {
        this.zutatID = zutatID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getKcalPro100g() {
        return kcalPro100g;
    }

    public void setKcalPro100g(Double kcalPro100g) {
        this.kcalPro100g = kcalPro100g;
    }

//    public Einheit getEinheit() {
//        return einheit;
//    }
//
//    public void setEinheit(Einheit einheit) {
//        this.einheit = einheit;
//    }
}

