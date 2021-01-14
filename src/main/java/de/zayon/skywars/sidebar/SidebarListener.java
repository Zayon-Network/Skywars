package de.zayon.skywars.sidebar;

import de.zayon.skywars.Skywars;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class SidebarListener implements Listener {
    private final Skywars skywars;

    public SidebarListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SidebarCache.removeCachedSidebar(player);
        this.skywars.getServer().getPluginManager().registerEvents(this, (Plugin)this.skywars);
    }
}
