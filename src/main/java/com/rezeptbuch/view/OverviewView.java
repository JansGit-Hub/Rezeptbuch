package com.rezeptbuch.view;

import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.service.RezepteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
public class OverviewView extends VerticalLayout {

    private Grid<Rezepte> grid = new Grid<>(Rezepte.class);
    private TextField searchField = new TextField("Suche Rezepte");
    private Button searchButton = new Button("Suchen");
    private RezepteService rezepteService;

    public OverviewView(RezepteService rezepteService) {
        this.rezepteService = rezepteService;

        HorizontalLayout searchLayout = new HorizontalLayout(searchField, searchButton);

        // Grid konfigurieren
        grid.setColumns("name", "zutaten", "anleitung");
        grid.addComponentColumn(rezept -> {
            Image recipeImage = new Image();

            recipeImage.setAlt("Rezept Bild");
            recipeImage.setWidth("100px");
            recipeImage.setHeight("100px");
            return recipeImage;
        }).setHeader("Bild");

        // Such-Button Funktion
        searchButton.addClickListener(event -> {
            String searchTerm = searchField.getValue();
            grid.setItems(rezepteService.searchRezepte(searchTerm)); // Methode im Service zum Filtern
        });

        // Initiale Rezepte laden
        updateGrid();

        // Übersicht anzeigen
        add(searchLayout, grid);
    }

    private void updateGrid() {
        grid.setItems(rezepteService.getAllRezepte()); // Alle Rezepte abrufen
    }
}

