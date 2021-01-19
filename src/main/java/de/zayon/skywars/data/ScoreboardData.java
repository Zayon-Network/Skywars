package de.zayon.skywars.data;



import de.zayon.skywars.sidebar.SidebarPage;

import java.util.Arrays;
import java.util.List;

public enum ScoreboardData implements SidebarPage {
    PAGE_1("§7» §2§lSkywars §8| 1/2", Arrays.asList("§r", "§8➤ §a§lTeam", "%team%", "§r", "§8➤ §a§lPhase", "%gamestatus%", "§r", "§8➤ §a§lZeit", "%time%", "§r", "§8➤ §a§lKit", "%kit%", "§r", "§7Zayon.de"));
//    PAGE_2("§7» §b§lSkywars §8| 2/2",Arrays.asList("§r", "§8➤ %item1%","§8➤ %item2%","§8➤ %item3%","§8➤ %item4%","§8➤ %item5%","§8➤ %item6%","§8➤ %item7%","§8➤ %item8%","§8➤ %item9%", "", "§7Zayon.de"));

    private final List<String> lines;

    private final String displayName;


    ScoreboardData(String displayName, List<String> lines) {
        this.displayName = displayName;
        this.lines = lines;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLines() {
        return this.lines;
    }
}
