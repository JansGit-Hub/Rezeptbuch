package com.rezeptbuch.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.PermitAll;

@Route("overview")
@PermitAll
public class OverView extends AppLayout {

    private FlexLayout recipeContainer;

    public OverView() {
        // Seiten-Navigation
        SideNav sideNav = new SideNav();
        sideNav.addItem(new SideNavItem("Startseite", OverView.class));
        sideNav.addItem(new SideNavItem("Rezepte", OverView.class));
        sideNav.addItem(new SideNavItem("Favoriten", OverView.class));

        // Suchfeld
        TextField searchField = new TextField("Suche nach Rezepten");
        Button searchButton = new Button("Suchen");

        Div searchContainer = new Div(searchField, searchButton);
        searchContainer.setWidth("100%");

        // Rezept-Kacheln
        recipeContainer = new FlexLayout();
        recipeContainer.getStyle().set("flex-wrap", "wrap"); // Automatischer Umbruch für Kacheln
        recipeContainer.setWidth("100%");
        addExampleRecipes();

        // Layout zusammenfügen
        VerticalLayout mainContent = new VerticalLayout(searchContainer, recipeContainer);
        addToDrawer(sideNav);
        addToNavbar(new DrawerToggle(), new H1("Rezeptbuch Übersicht"));
        setContent(mainContent);
    }

    private void addExampleRecipes() {
        String exampleImageUrl = "https://www.example.com/sample-recipe-image.jpg"; // Ersetze mit einer echten Bild-URL

        for (int i = 1; i <= 5; i++) {
            Div card = new Div();
            Image image = new Image(exampleImageUrl, "Beispielrezept " + i);
            image.setWidth("200px");
            image.setHeight("150px");

            card.add(image);
            card.add(new H3("Rezept " + i));
            card.add(new Paragraph("Kategorie: Beispielkategorie"));

            card.getStyle()
                    .set("border", "1px solid #ddd")
                    .set("padding", "10px")
                    .set("margin", "10px")
                    .set("text-align", "center");

            recipeContainer.add(card);
        }
    }
}
