package com.rezeptbuch.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.rezeptbuch.service.CustomUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterView(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;

        Div card = new Div();
        card.getStyle().set("padding", "20px")
                .set("border-radius", "8px")
                .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)")
                .set("background-color", "white")
                .set("max-width", "400px");

        var usernameField = new TextField("Benutzername");
        var passwordField = new PasswordField("Passwort");

        var registerButton = new Button("Registrieren", click -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                showNotification("Bitte füllen Sie alle Felder aus!", NotificationVariant.LUMO_ERROR);
                return;
            }

            if (userDetailsService.userExists(username)) {
                showNotification("Benutzername ist bereits vergeben! Vorschlag: " + username + "123", NotificationVariant.LUMO_ERROR);
                return;
            }

            userDetailsService.createUser(username, passwordEncoder.encode(password), "USER");
            showNotification("Registrierung erfolgreich!", NotificationVariant.LUMO_SUCCESS);
            usernameField.clear();
            passwordField.clear();

            UI.getCurrent().getPage().executeJs("setTimeout(() => { window.location.href='login'; }, 2000);");
        });

        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout inputLayout = new VerticalLayout(usernameField, passwordField, registerButton);
        inputLayout.setSpacing(true);
        inputLayout.setAlignItems(Alignment.CENTER);

        card.add(new H1("Registrieren"), inputLayout);
        add(card);
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = new Notification(message, 3000);
        notification.addThemeVariants(variant);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }
}
