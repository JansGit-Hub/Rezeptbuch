package com.rezeptbuch.view;
import com.rezeptbuch.model.Rezepte;
import com.rezeptbuch.service.RezepteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@Route("")
@AnonymousAllowed
public class OverView extends AppLayout {

    private final RezepteService rezepteService;
    private FlexLayout recipeContainer;

    @Autowired
    public OverView(RezepteService rezepteService) {
        this.rezepteService = rezepteService;

        H1 title = new H1("Rezeptbuch Übersicht");
        title.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-l)").set("line-height", "var(--lumo-size-l)");

        HorizontalLayout actionButtons = createActionButtons();
        HorizontalLayout navbar = new HorizontalLayout(new DrawerToggle(), title, actionButtons);
        navbar.expand(title);
        navbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navbar.setWidthFull();
        navbar.setSpacing(true);
        addToNavbar(navbar);

        TextField searchField = new TextField("Suche nach Rezepten");
        Button searchButton = new Button("Suchen");
        ComboBox<String> filterField = new ComboBox<>("Filter");
        filterField.setItems("Alle", "Vegetarisch", "Vegan", "Glutenfrei", "Low-Carb");

        Div searchContainer = new Div();
        searchContainer.getStyle().set("display", "flex").set("gap", "10px").set("align-items", "center").set("width", "100%");
        searchButton.getStyle().set("align-self", "flex-end");
        searchField.setWidth("250px");
        filterField.setWidth("150px");
        searchButton.setHeight("40px");
        searchContainer.add(searchField, filterField, searchButton);

        recipeContainer = new FlexLayout();
        recipeContainer.getStyle().set("flex-wrap", "wrap");
        recipeContainer.setWidth("100%");

        VerticalLayout mainContent = new VerticalLayout(searchContainer, recipeContainer);
        addToDrawer(new NavigationMenu());
        setContent(mainContent);

        loadRecipesFromDatabase();

    }
    private void loadRecipesFromDatabase() {
        List<Rezepte> rezepteList = rezepteService.getAllRezepte();
        recipeContainer.removeAll();

        for (Rezepte rezept : rezepteList) {
            Div card = new Div();
            card.setWidth("220px");
            card.setHeight("300px");
            card.getStyle()
                    .set("display", "flex")
                    .set("flex-direction", "column")
                    .set("justify-content", "center")
                    .set("align-items", "center")
                    .set("border", "1px solid #ddd")
                    .set("padding", "10px")
                    .set("margin", "10px")
                    .set("text-align", "center")
                    .set("cursor", "pointer")
                    .set("border-radius", "10px")
                    .set("box-shadow", "2px 2px 8px rgba(0, 0, 0, 0.2)")
                    .set("transition", "0.3s ease-in-out");

            card.getElement().executeJs(
                    "this.addEventListener('mouseover', function() { this.style.backgroundColor = '#f8f8f8'; this.style.boxShadow = '4px 4px 12px rgba(0, 0, 0, 0.3)'; });" +
                            "this.addEventListener('mouseout', function() { this.style.backgroundColor = 'white'; this.style.boxShadow = '2px 2px 8px rgba(0, 0, 0, 0.2)'; });"
            );


            card.addClickListener(event -> {
                UI.getCurrent().navigate("rezepte-ansehen/" + rezept.getId());
            });

            Image image = new Image();
            image.setWidth("200px");
            image.setHeight("150px");

            H3 title = new H3(rezept.getName());
            title.getStyle().set("margin", "5px 0");

            Paragraph category = new Paragraph("Kategorie: " + rezept.getKategorie());
            category.getStyle().set("font-size", "14px").set("margin", "5px 0");

            card.add(image, title, category);
            recipeContainer.add(card);
        }
    }

    private HorizontalLayout createActionButtons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                !(authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")));

        if (isAuthenticated) {
            Button logoutButton = new Button("Logout", e -> {
                UI.getCurrent().navigate("/");
                VaadinSession.getCurrent().close();
                VaadinSession.getCurrent().getSession().invalidate();
                SecurityContextHolder.clearContext();
            });
            logoutButton.getStyle().set("color", "red");
            actions.add(logoutButton);
        } else {
            Button loginButton = new Button("Login", e -> {
                UI.getCurrent().navigate("login");
            });
            Button registerButton = new Button("Registrieren", e -> UI.getCurrent().navigate("register"));
            actions.add(loginButton, registerButton);
        }

        return actions;
    }

}