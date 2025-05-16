package com.rezeptbuch.view;

import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.service.RezepteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Route("einkaufsliste")
@PermitAll

public class EinkaufslisteView extends VerticalLayout {

    private final RezepteService rezepteService;

    // Einkaufsliste
    private final List<String> gemeinsameListe = new ArrayList<>();
    private final Map<Rezepte, List<String>> rezeptZutatenMap = new LinkedHashMap<>();

    private final VerticalLayout zutatenLayout = new VerticalLayout();
    private boolean gruppiert = true;

    @Autowired
    public EinkaufslisteView(RezepteService rezepteService) {
        this.rezepteService = rezepteService;

        add(new H2("Einkaufsliste"));

        // Rezeptauswahl
        Select<Rezepte> rezeptSelect = new Select<>();
        rezeptSelect.setLabel("Rezept hinzufügen");
        rezeptSelect.setItems(rezepteService.getAllRezepte());
        rezeptSelect.setItemLabelGenerator(Rezepte::getName);

        rezeptSelect.addValueChangeListener(event -> {
            Rezepte rezept = event.getValue();
            if (rezept != null && rezept.getZutaten() != null) {
                List<String> zutaten = parseZutaten(rezept.getZutaten());
                gemeinsameListe.addAll(zutaten);
                rezeptZutatenMap.put(rezept, zutaten);
                updateZutatenView();
            }
        });

        Button filterButton = new Button("Ansicht wechseln", click -> {
            gruppiert = !gruppiert;
            updateZutatenView();
        });

        add(new HorizontalLayout(rezeptSelect, filterButton));
        add(zutatenLayout);
    }

    private void updateZutatenView() {
        zutatenLayout.removeAll();

        if (gruppiert) {
            Map<String, Long> grouped = gemeinsameListe.stream()
                    .collect(Collectors.groupingBy(z -> z, LinkedHashMap::new, Collectors.counting()));

            grouped.forEach((zutat, count) -> {
                Checkbox checkbox = new Checkbox(zutat + (count > 1 ? " (" + count + "x)" : ""));
                zutatenLayout.add(checkbox);
            });

        } else {
            rezeptZutatenMap.forEach((rezept, zutaten) -> {
                zutatenLayout.add(new H4(rezept.getName()));
                zutaten.forEach(z -> {
                    Checkbox checkbox = new Checkbox(z);
                    zutatenLayout.add(checkbox);
                });
            });
        }
    }

    private List<String> parseZutaten(String zutatenText) {
        return Arrays.stream(zutatenText.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }
}
