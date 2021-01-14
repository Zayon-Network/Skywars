package de.zayon.skywars.sidebar;

import java.util.List;

public interface SidebarPage {
    String getDisplayName();

    List<String> getLines();
}
