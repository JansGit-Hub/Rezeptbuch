//Hugo Pina Garcia
package com.rezeptbuch.model;

import com.rezeptbuch.model.Zutat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZutatRepository extends JpaRepository<Zutat, Long> {
    List<Zutat> findByNameContainingIgnoreCase(String name);
}

