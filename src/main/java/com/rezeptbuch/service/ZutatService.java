//Hugo Pina Garcia
package com.rezeptbuch.service;

import com.rezeptbuch.model.Zutat;
import com.rezeptbuch.model.ZutatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZutatService {

    private final ZutatRepository zutatRepository;

    @Autowired
    public ZutatService(ZutatRepository zutatRepository) {
        this.zutatRepository = zutatRepository;
    }

    // Neue Zutat speichern
    public Zutat saveZutat(Zutat zutat) {
        if (zutat.getName() == null || zutat.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name der Zutat darf nicht leer sein.");
        }
        return zutatRepository.save(zutat);
    }

    // Alle Zutaten abrufen
    public List<Zutat> getAllZutaten() {
        return zutatRepository.findAll();
    }

    // Zutat nach ID abrufen
    public Optional<Zutat> getZutatById(Long id) {
        return zutatRepository.findById(id);
    }

    // Zutat löschen
    public void deleteZutat(Long id) {
        if (!zutatRepository.existsById(id)) {
            throw new IllegalArgumentException("Zutat mit der ID " + id + " existiert nicht.");
        }
        zutatRepository.deleteById(id);
    }

    // Optional: Nach Name suchen (wenn du das im Repository ergänzen willst)
    // public List<Zutat> searchZutaten(String name) {
    //     return zutatRepository.findByNameContainingIgnoreCase(name);
    // }
}
