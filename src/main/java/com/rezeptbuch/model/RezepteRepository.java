package com.rezeptbuch.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RezepteRepository extends JpaRepository<Rezepte, Long> {
    List<Rezepte> findByNameContainingIgnoreCase(String name);
}

