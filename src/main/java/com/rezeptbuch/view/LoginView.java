package com.rezeptbuch.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        System.out.println("LoginView wird geöffnet");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setTitle("Rezeptbuch");
        loginOverlay.setDescription("Bitte geben Sie Ihre Login-Daten ein.");
        loginOverlay.setAction("login");

        Button registerButton = new Button("Registrieren", click -> {
            UI.getCurrent().navigate("register");
            loginOverlay.setOpened(false);
        });

        registerButton.getStyle()
                .set("background-color", "#007BFF")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "10px 20px")
                .set("width", "100%")
                .set("border-radius", "5px");


        loginOverlay.getFooter().add(registerButton);

        UI.getCurrent().getPage().fetchCurrentURL(url -> {
            String query = url.getQuery();
            if (query != null && query.contains("error")) {
                loginOverlay.setError(true);
            }
        });


        loginOverlay.setOpened(true);

        loginOverlay.addLoginListener(event -> {
            Notification.show("Anmeldung erfolgreich: " + event.getUsername(), 3000, Notification.Position.TOP_CENTER);
        });
    }
}

