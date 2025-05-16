package com.rezeptbuch.model;

import jakarta.persistence.*;

@Entity
public class Rezepte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Lob
    private String zutaten;
    @Lob
    private String anleitung;
    private Integer portionen;
    private String kategorie;
    private String bildUrl;

    public Rezepte() {}

    public Rezepte(String name, String zutaten, String anleitung) {
        this.name = name;
        this.zutaten = zutaten;
        this.anleitung = anleitung;
        this.portionen = portionen;
        this.kategorie = kategorie;
        this.bildUrl = bildUrl;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZutaten() {
        return zutaten;
    }

    public void setZutaten(String zutaten) {
        this.zutaten = zutaten;
    }

    public String getAnleitung() {
        return anleitung;
    }

    public void setAnleitung(String anleitung) {
        this.anleitung = anleitung;
    }
    public int getPortionen() { return portionen; }

    public void setPortionen(int portionen) { this.portionen = portionen; }

    public String getKategorie() { return kategorie; }

    public void setKategorie(String kategorie) { this.kategorie = kategorie; }

    public String getBildUrl() { return bildUrl; }

    public void setBildUrl(String bildUrl) { this.bildUrl = bildUrl; }

}
