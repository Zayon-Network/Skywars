package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final Skywars skywars;
    public PlayerMoveListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        if(GameState.state.equals(GameState.LOBBY)) {
            if(event.getPlayer().getLocation().getY() < 10) {
                event.getPlayer().teleport(new Location(Bukkit.getWorld("WLobby"), 152.5, 52, 148.5, 60, 0));
            }
        }
        return;
    }
}
