package com.prancer.dance.base.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route
public final class MainView extends Main {

    // TODO Replace with your own main view.

    MainView() {
        addClassName(LumoUtility.Padding.MEDIUM);
        add(new Div("Please select a view from the menu on the left."));
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}
