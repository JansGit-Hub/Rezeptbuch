package com.rezeptbuch.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
public class OverView extends AppLayout {
    private FlexLayout recipeContainer;

    public OverView() {

        TextField searchField = new TextField("Suche nach Rezepten");
        Button searchButton = new Button("Suchen");
        ComboBox<String> filterField = new ComboBox<>("Filter");
        filterField.setItems("Alle", "Vegetarisch", "Vegan", "Glutenfrei", "Low-Carb");

        Div searchContainer = new Div();
        searchContainer.getStyle().set("display", "flex");
        searchContainer.getStyle().set("gap", "10px");
        searchContainer.getStyle().set("align-items", "center");
        searchContainer.getStyle().set("width", "100%");
        searchButton.getStyle().set("align-self", "flex-end");
        searchField.setWidth("250px");
        filterField.setWidth("150px");
        searchButton.setHeight("40px");

        searchContainer.add(searchField, filterField, searchButton);



        recipeContainer = new FlexLayout();
        recipeContainer.getStyle().set("flex-wrap", "wrap");
        recipeContainer.setWidth("100%");
        addExampleRecipes();

        VerticalLayout mainContent = new VerticalLayout(searchContainer, recipeContainer);
        addToDrawer(new NavigationMenu());
        addToNavbar(new DrawerToggle(), new H1("Rezeptbuch Übersicht"));
        setContent(mainContent);
    }

    private void addExampleRecipes() {
        String exampleImageUrl = "https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&w=1200";

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
