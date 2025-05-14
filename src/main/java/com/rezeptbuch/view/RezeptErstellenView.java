package com.rezeptbuch.view;

import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.model.RezepteRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


import java.util.ArrayList;
import java.util.List;

@Route("rezept-erstellen")
@PermitAll
public class RezeptErstellenView extends VerticalLayout {
    private final RezepteRepository rezepteRepository;

    private final TextField titelField = new TextField("Titel*");
    private final TextArea beschreibungField = new TextArea("Beschreibung*");
    private final ComboBox<String> kategorieBox = new ComboBox<>("Kategorie*");
    private final TextField portionenField = new TextField("Portionen");
    private final Button minusButton = new Button("−");
    private final Button plusButton = new Button("+");
    private final Upload bildUpload = new Upload();
    private final Image bildPreview = new Image();
    private final Button zutatHinzufuegenButton = new Button("+ Zutat hinzufügen");
    private final TextField tagField = new TextField("+ Tag hinzufügen");
    private final HorizontalLayout tagAnzeige = new HorizontalLayout();
    private final Button speichernButton = new Button("Speichern");
    private final Button abbrechenButton = new Button("Abbrechen");

    private int portionen = 4; // Startwert
    private final List<String> tags = new ArrayList<>();

    public RezeptErstellenView(RezepteRepository rezepteRepository) {
        this.rezepteRepository = rezepteRepository;

        setSpacing(true);
        setPadding(true);

        // Kategorie-Auswahl
        kategorieBox.setItems("Vorspeise", "Hauptspeise", "Nachspeise", "Snack");

        // Portionen-Steuerung
        portionenField.setValue(String.valueOf(portionen));
        portionenField.setReadOnly(true);

        minusButton.addClickListener(e -> {
            if (portionen > 1) {
                portionen--;
                portionenField.setValue(String.valueOf(portionen));
            }
        });

        plusButton.addClickListener(e -> {
            if (portionen < 100) {
                portionen++;
                portionenField.setValue(String.valueOf(portionen));
            }
        });

        HorizontalLayout portionenLayout = new HorizontalLayout(minusButton, portionenField, plusButton);
        portionenLayout.setAlignItems(Alignment.BASELINE);

        // Bild Upload
        bildUpload.setAcceptedFileTypes("image/*");
        bildPreview.setMaxWidth("200px");

        // Tag-Handling
        tagField.addValueChangeListener(event -> {
            String tag = event.getValue();
            if (!tag.isEmpty() && !tags.contains(tag)) {
                tags.add(tag);
                Span tagChip = new Span(tag);
                tagChip.getStyle().set("background", "#007bff");
                tagChip.getStyle().set("color", "white");
                tagChip.getStyle().set("padding", "0.2em 0.6em");
                tagChip.getStyle().set("border-radius", "10px");
                tagAnzeige.add(tagChip);
                tagField.clear();
            }
        });

        // Speichern
        speichernButton.addClickListener(e -> {
            String titel = titelField.getValue();
            String beschreibung = beschreibungField.getValue();
            String kategorie = kategorieBox.getValue();
            String portionenText = portionenField.getValue();

            if (titel.isEmpty() || beschreibung.isEmpty() || kategorie == null) {
                Notification.show("Bitte alle mit * markierten Felder ausfüllen", 3000, Notification.Position.MIDDLE);
                return;
            }

            Rezepte rezept = new Rezepte();
            rezept.setName(titel);
            rezept.setAnleitung(beschreibung);
            rezept.setKategorie(kategorie);
            rezept.setPortionen(Integer.parseInt(portionenText));
            rezept.setZutaten("Noch keine Zutaten erfasst"); // TODO: Zutaten dynamisch erfassen
            rezept.setBildUrl("Noch kein Bild"); // Placeholder

            rezepteRepository.save(rezept);

            Notification.show("Rezept gespeichert!", 3000, Notification.Position.TOP_CENTER);

            // Felder zurücksetzen
            titelField.clear();
            beschreibungField.clear();
            kategorieBox.clear();
            portionen = 4;
            portionenField.setValue(String.valueOf(portionen));
            tagAnzeige.removeAll();
            tags.clear();
        });

        abbrechenButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("start")); // TODO: Zielroute definieren
        });

        // Layout-Zusammenstellung
        HorizontalLayout topRow = new HorizontalLayout(titelField, bildUpload);
        topRow.setAlignItems(Alignment.CENTER);
        HorizontalLayout midRow = new HorizontalLayout(beschreibungField, portionenLayout);
        midRow.setAlignItems(Alignment.CENTER);
        HorizontalLayout bottomRow = new HorizontalLayout(kategorieBox, tagField);
        bottomRow.setAlignItems(Alignment.CENTER);
        HorizontalLayout actionRow = new HorizontalLayout(abbrechenButton, speichernButton);
        actionRow.setSpacing(true);

        add(
                new H2("Rezept erstellen"),
                topRow,
                zutatHinzufuegenButton,
                midRow,
                bottomRow,
                tagAnzeige,
                new Hr(),
                actionRow
        );
    }
}
