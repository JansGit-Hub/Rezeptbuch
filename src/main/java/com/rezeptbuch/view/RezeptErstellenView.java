package com.rezeptbuch.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Hr;


import java.util.ArrayList;
import java.util.List;

@Route("rezept-erstellen")
@PermitAll
public class RezeptErstellenView extends VerticalLayout {

    private final TextField titelField = new TextField("Titel");
    private final TextArea beschreibungField = new TextArea("Beschreibung");
    private final ComboBox<String> kategorieBox = new ComboBox<>("Kategorie");

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
    private final Button loeschenButton = new Button("Löschen");

    private int portionen = 4; // Startwert

    public RezeptErstellenView() {
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
            portionen++;
            portionenField.setValue(String.valueOf(portionen));
        });

        HorizontalLayout portionenLayout = new HorizontalLayout(minusButton, portionenField, plusButton);
        portionenLayout.setAlignItems(Alignment.BASELINE);

        // Bild Upload
        bildUpload.setAcceptedFileTypes("image/*");
        bildPreview.setMaxWidth("200px");

        // Tag-Handling
        List<String> tags = new ArrayList<>();
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

        // Aktionen
        speichernButton.addClickListener(e -> Notification.show("Rezept gespeichert (Platzhalter)"));

        abbrechenButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("overview")); // oder Startseite
        });

        loeschenButton.addClickListener(e -> {
            Notification.show("Rezept gelöscht (Platzhalter)");
        });
        loeschenButton.getStyle().set("color", "red");

        // Layout-Zusammenstellung
        HorizontalLayout topRow = new HorizontalLayout(titelField, bildUpload);
        HorizontalLayout catRow = new HorizontalLayout(kategorieBox, portionenLayout);

        HorizontalLayout actionRow = new HorizontalLayout(abbrechenButton, speichernButton);
        actionRow.setSpacing(true);

        add(
                new H2("Rezept erstellen"),
                topRow,
                beschreibungField,
                zutatHinzufuegenButton,
                catRow,
                tagField,
                tagAnzeige,
                new Hr(),
                loeschenButton,
                actionRow
        );
    }
}
