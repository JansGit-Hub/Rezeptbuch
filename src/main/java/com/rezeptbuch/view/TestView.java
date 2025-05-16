package com.rezeptbuch.view;

import com.rezeptbuch.model.Token;
import com.rezeptbuch.service.AuthenticationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Route("test")
@PermitAll
@PageTitle("Test View")
public class TestView extends VerticalLayout {
    private final AuthenticationService authenticationService;
    public TestView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        authenticationService.verifyUserAccess();

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("Willkommen in der TestView!");
        add(header);
    }
}
