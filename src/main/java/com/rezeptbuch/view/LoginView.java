package com.rezeptbuch.view;

import com.rezeptbuch.model.Token;
import com.rezeptbuch.model.User;
import com.rezeptbuch.service.AuthenticationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Route("login")
@AnonymousAllowed
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private final AuthenticationService authenticationService;

    public LoginView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div container = new Div();
        container.getStyle()
                .set("padding", "20px")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
                .set("background-color", "#ffffff")
                .set("max-width", "400px")
                .set("margin", "auto");

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.CENTER);

        H1 header = new H1("Login");
        header.getStyle().set("margin-bottom", "10px");

        TextField usernameField = new TextField("Benutzername");
        usernameField.setRequiredIndicatorVisible(true);

        PasswordField passwordField = new PasswordField("Passwort");
        passwordField.setRequiredIndicatorVisible(true);

        Button loginButton = new Button("Login");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidth("100%");

        loginButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                showErrorNotification("Benutzername und Passwort dürfen nicht leer sein!");
                return;
            }

            boolean passwordValid = authenticationService.validatePassword(username, password);
            if (!passwordValid) {
                showErrorNotification("Falsches Passwort oder Benutzer existiert nicht!");
                return;
            }

            Optional<Token> tokenOpt = authenticationService.findUserToken(username);
            if (tokenOpt.isPresent() && !authenticationService.isTokenConfirmed(tokenOpt.get().getToken())) {
                showErrorNotification("Bitte verifiziere dich zuerst über deine E-Mail!");
                return;
            }

            User user = authenticationService.getUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UI.getCurrent().navigate("");
        });


        formLayout.add(header, usernameField, passwordField, loginButton);
        container.add(formLayout);
        add(container);
    }

    private void showErrorNotification(String message) {
        Notification notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}

