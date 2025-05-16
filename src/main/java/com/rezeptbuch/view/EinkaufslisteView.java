package com.rezeptbuch.view;

import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.service.RezepteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
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

    private final List<String> gemeinsameListe = new ArrayList<>();
    private final Map<Rezepte, List<String>> rezeptZutatenMap = new LinkedHashMap<>();

    private final VerticalLayout zutatenLayout = new VerticalLayout();
    private boolean gruppiert = true;

    @Autowired
    public EinkaufslisteView(RezepteService rezepteService) {
        this.rezepteService = rezepteService;

        add(new H2("Einkaufsliste"));

        Select<Rezepte> rezeptSelect = new Select<>();
        rezeptSelect.setLabel("Rezept auswählen");
        rezeptSelect.setItems(rezepteService.getAllRezepte());
        rezeptSelect.setItemLabelGenerator(Rezepte::getName);

        Button addButton = new Button("Rezept zur Liste hinzufügen", event -> {
            Rezepte selectedRezept = rezeptSelect.getValue();
            if (selectedRezept != null && selectedRezept.getZutaten() != null) {
                List<String> zutaten = parseZutaten(selectedRezept.getZutaten());
                gemeinsameListe.addAll(zutaten);
                rezeptZutatenMap.put(selectedRezept, zutaten);
                updateZutatenView();
                Notification.show("Rezept \"" + selectedRezept.getName() + "\" hinzugefügt.");
            } else {
                Notification.show("Bitte wähle zuerst ein Rezept aus.");
            }
        });

        Button filterButton = new Button("Ansicht wechseln", click -> {
            gruppiert = !gruppiert;
            updateZutatenView();
        });

        Button printButton = new Button("Einkaufsliste drucken", event -> {
            StringBuilder printContent = new StringBuilder();

            if (gruppiert) {
                Map<String, Long> grouped = gemeinsameListe.stream()
                        .collect(Collectors.groupingBy(z -> z, LinkedHashMap::new, Collectors.counting()));

                printContent.append("<h3>Gemeinsame Einkaufsliste</h3><ul>");
                grouped.forEach((zutat, count) -> {
                    printContent.append("<li>")
                            .append(zutat)
                            .append(count > 1 ? " (" + count + "x)" : "")
                            .append("</li>");
                });
                printContent.append("</ul>");
            } else {
                rezeptZutatenMap.forEach((rezept, zutaten) -> {
                    printContent.append("<h4>").append(rezept.getName()).append("</h4><ul>");
                    zutaten.forEach(z -> printContent.append("<li>").append(z).append("</li>"));
                    printContent.append("</ul>");
                });
            }

            String content = printContent.toString();

            getElement().executeJs("""
                const content = $0;
                const printWindow = window.open('', '', 'width=800,height=600');
                printWindow.document.write('<html><head><title>Einkaufsliste</title></head><body>' + content + '</body></html>');
                printWindow.document.close();
                printWindow.focus();
                printWindow.print();
                printWindow.close();
            """, content);
        });

        add(new HorizontalLayout(rezeptSelect, addButton, filterButton, printButton));
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
