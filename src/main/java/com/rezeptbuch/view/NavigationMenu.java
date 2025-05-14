package com.rezeptbuch.view;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
//Test
public class NavigationMenu extends SideNav {
    public NavigationMenu() {
        addItem(new SideNavItem("Startseite", OverView.class));
        addItem(new SideNavItem("Rezepte", RezeptErstellenView.class));
        addItem(new SideNavItem("Favoriten", OverView.class));
    }
}

