package com.rezeptbuch.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("verify-email")
@PageTitle("Almost Done")
@AnonymousAllowed
public class VerifyEmailView extends VerticalLayout {

    public VerifyEmailView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div container = new Div();
        container.getStyle()
                .set("padding", "20px")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
                .set("background-color", "lightgreen")
                .set("max-width", "400px")
                .set("text-align", "center")
                .set("margin", "auto");

        H2 header = new H2("Almost Done");
        Paragraph message = new Paragraph("Please verify your email to login and use all functions.");

        container.add(header, message);
        add(container);
    }
}
