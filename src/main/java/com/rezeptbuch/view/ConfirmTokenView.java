package com.rezeptbuch.view;

import com.rezeptbuch.model.Token;
import com.rezeptbuch.model.User;
import com.rezeptbuch.service.EmailService;
import com.rezeptbuch.service.TokenService;
import com.rezeptbuch.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import java.util.Optional;

@Route("register/confirmToken")
@AnonymousAllowed
@PageTitle("Confirm Token")
public class ConfirmTokenView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private TextField usernameField;
    private String token;

    public ConfirmTokenView(UserService userService, TokenService tokenService, EmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        usernameField = new TextField("Username");
        Button renewTokenButton = new Button("Neuen Token anfordern", event -> renewToken());

        add(usernameField, renewTokenButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
            token = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("token", List.of()).stream().findFirst().orElse(null);
        if (token != null && !token.isEmpty()) {
            try {
                userService.confirmToken(token);
                add(new H1("Token erfolgreich bestätigt!"));
            } catch (IllegalArgumentException | IllegalStateException e) {
                add(new H1(e.getMessage()));
            }
        } else {
            add(new H1("Kein Token angegeben, fordere einen neuen an um alle Features zu aktivieren!"));
        }

        RouterLink loginLink = new RouterLink("Zum Login", LoginView.class);
        add(loginLink);
    }

    private void renewToken() {
        String username = usernameField.getValue();

        if (username.isEmpty()) {
            Notification.show("Bitte Benutzernamen eingeben!", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            Notification.show("Benutzer nicht gefunden!", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        Optional<Token> tokenOpt = tokenService.findByTokenForUser(user);
        if (tokenOpt.isEmpty()) {
            Notification.show("Token nicht gefunden!", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        Token newToken = tokenService.renewExistingToken(tokenOpt.get());
        emailService.sendSimpleMail(user.getEmail(), newToken.getToken());

        Notification.show("Neuer Token wurde gesendet!", 3000, Notification.Position.TOP_CENTER);
    }

}

