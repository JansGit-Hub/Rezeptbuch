package com.rezeptbuch.view;

import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.model.RezepteRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("rezepte")
@PermitAll
public class RezepteView extends VerticalLayout {

    private RezepteRepository rezepteRepository;
    private Grid<Rezepte> grid = new Grid<>(Rezepte.class);

    private TextField nameField = new TextField("Name");
    private TextField zutatenField = new TextField("Zutaten");
    private TextField anleitungField = new TextField("Anleitung");
    private Button addButton = new Button("Rezept hinzufügen");

    @Autowired
    public RezepteView(RezepteRepository rezepteRepository) {
        this.rezepteRepository = rezepteRepository;

        // Grid konfigurieren
        grid.setColumns("id", "name", "zutaten", "anleitung");

        // Rezept hinzufügen
        addButton.addClickListener(event -> {
            Rezepte rezept = new Rezepte(
                    nameField.getValue(),
                    zutatenField.getValue(),
                    anleitungField.getValue()
            );
            rezepteRepository.save(rezept);
            updateGrid();
            Notification.show("Rezept hinzugefügt!");
        });

        // Layout
        add(nameField, zutatenField, anleitungField, addButton, grid);

        // Grid initialisieren
        updateGrid();
    }

    private void updateGrid() {
        grid.setItems(rezepteRepository.findAll());
    }
}

