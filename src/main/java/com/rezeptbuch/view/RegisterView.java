package com.rezeptbuch.view;

import com.rezeptbuch.model.User;
import com.rezeptbuch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.NavigationEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@PageTitle("Please sign up")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final UserService userService;

    public RegisterView(UserService userService) {
        this.userService = userService;

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

        H2 header = new H2("Please sign up");

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.CENTER);

        TextField firstName = new TextField("First Name");
        firstName.setRequiredIndicatorVisible(true);

        TextField lastName = new TextField("Last Name");
        lastName.setRequiredIndicatorVisible(true);

        TextField username = new TextField("Username");
        username.setRequiredIndicatorVisible(true);

        EmailField email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Please enter a valid email address!");

        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);

        Button registerButton = new Button("Sign up", new Icon(VaadinIcon.CHECK));
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.setWidth("100%");

        registerButton.addClickListener(event -> registerUser(firstName, lastName, username, email, password));

        formLayout.add(firstName, lastName, username, email, password);
        container.add(header, formLayout, registerButton);

        add(container);
    }


    private void registerUser(TextField firstName, TextField lastName, TextField username, EmailField email, PasswordField password) {
        boolean hasError = false;

        if (firstName.isEmpty()) {
            firstName.setInvalid(true);
            firstName.setErrorMessage("First Name is required!");
            hasError = true;
        }

        if (lastName.isEmpty()) {
            lastName.setInvalid(true);
            lastName.setErrorMessage("Last Name is required!");
            hasError = true;
        }

        if (username.isEmpty()) {
            username.setInvalid(true);
            username.setErrorMessage("Username is required!");
            hasError = true;
        }

        if (email.isEmpty() || email.isInvalid()) {
            email.setInvalid(true);
            email.setErrorMessage("Please enter a valid email address!");
            hasError = true;
        }

        if (password.isEmpty()) {
            password.setInvalid(true);
            password.setErrorMessage("Password is required!");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        User user = new User();
        user.setFirstName(firstName.getValue());
        user.setLastName(lastName.getValue());
        user.setUsername(username.getValue());
        user.setEmail(email.getValue());
        user.setPassword(password.getValue());

        userService.registerUser(user);

        UI.getCurrent().navigate("verify-email");

    }
}
