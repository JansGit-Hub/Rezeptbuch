package com.rezeptbuch.view;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Route("register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public RegisterView(JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;

        var usernameField = new TextField("Benutzername");
        var passwordField = new PasswordField("Passwort");
        var registerButton = new Button("Registrieren", click -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Bitte füllen Sie alle Felder aus!");
                return;
            }

            if (userDetailsManager.userExists(username)) {
                Notification.show("Benutzername ist bereits vergeben!");
                return;
            }

            userDetailsManager.createUser(
                    User.withUsername(username)
                            .password(passwordEncoder.encode(password))
                            .roles("USER")
                            .build()
            );
            Notification.show("Registrierung erfolgreich!");
            usernameField.clear();
            passwordField.clear();
            UI.getCurrent().navigate("login");
        });

        add(new H1("Registrieren"), usernameField, passwordField, registerButton);
    }
}
