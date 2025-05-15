package com.rezeptbuch.view;

import com.rezeptbuch.service.UserService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;

@Route("register/confirmToken")
@AnonymousAllowed
@PageTitle("Confirm Token")
public class ConfirmTokenView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;

    public ConfirmTokenView(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String token = event.getLocation().getQueryParameters().getParameters().getOrDefault("token", List.of()).stream().findFirst().orElse(null);

        if (token != null && !token.isEmpty()) {
            try {
                userService.confirmToken(token);
                add(new H1("Token erfolgreich bestätigt!"));
            } catch (IllegalArgumentException | IllegalStateException e) {
                add(new H1(e.getMessage()));
            }
        } else {
            add(new H1("Kein Token angegeben!"));
        }

        RouterLink loginLink = new RouterLink("Zum Login", LoginView.class);
        add(loginLink);
    }
}


