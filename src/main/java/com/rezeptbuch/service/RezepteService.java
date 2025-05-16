package com.rezeptbuch.service;

import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.model.RezepteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RezepteService {

    private final RezepteRepository rezepteRepository;

    @Autowired
    public RezepteService(RezepteRepository rezepteRepository) {
        this.rezepteRepository = rezepteRepository;
    }

    public Rezepte saveRezept(Rezepte rezept) {
        if (rezept.getName() == null || rezept.getName().isEmpty()) {
            throw new IllegalArgumentException("Der Name des Rezepts darf nicht leer sein.");
        }
        return rezepteRepository.save(rezept);
    }

    // Alle Rezepte abrufen
    public List<Rezepte> getAllRezepte() {
        return rezepteRepository.findAll();
    }

    // Rezept nach ID abrufen
    public Optional<Rezepte> getRezeptById(Long id) {
        return rezepteRepository.findById(id);
    }

    // Rezept nach Name suchen
    public List<Rezepte> searchRezepte(String searchTerm) {
        return rezepteRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    // Rezept löschen
    public void deleteRezept(Long id) {
        if (!rezepteRepository.existsById(id)) {
            throw new IllegalArgumentException("Rezept mit der ID " + id + " existiert nicht.");
        }
        rezepteRepository.deleteById(id);
    }
}

