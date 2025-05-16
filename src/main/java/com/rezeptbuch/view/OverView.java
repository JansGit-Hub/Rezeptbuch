package com.rezeptbuch.view;

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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
@AnonymousAllowed
public class OverView extends AppLayout {
    private FlexLayout recipeContainer;

    public OverView() {
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
        addExampleRecipes();

        VerticalLayout mainContent = new VerticalLayout(searchContainer, recipeContainer);
        addToDrawer(new NavigationMenu());
        setContent(mainContent);
    }

    private HorizontalLayout createActionButtons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                !(authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")));

        if (isAuthenticated) {
            Notification notification = Notification.show("Login erfolgreich! Willkommen " + authentication.getName());
            System.out.println("ja");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            Button logoutButton = new Button("Logout", e -> {
                UI.getCurrent().navigate("/");
                VaadinSession.getCurrent().close();
                VaadinSession.getCurrent().getSession().invalidate();
                SecurityContextHolder.clearContext();

            });
            logoutButton.getStyle().set("color", "red");
            actions.add(logoutButton);
        } else {
            System.out.println("nein");
            authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentifiziert: " + authentication.isAuthenticated());
            System.out.println("Authorities: " + authentication.getAuthorities());
            System.out.println("Principal: " + authentication.getPrincipal());
            Button loginButton = new Button("Login", e -> UI.getCurrent().navigate("login"));
            Button registerButton = new Button("Registrieren", e -> UI.getCurrent().navigate("register"));
            actions.add(loginButton, registerButton);
        }

        return actions;
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

            card.getStyle().set("border", "1px solid #ddd").set("padding", "10px").set("margin", "10px").set("text-align", "center");

            recipeContainer.add(card);
        }
    }
}