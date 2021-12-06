package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameData;
import de.nehlen.skywars.data.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener {

    private final Skywars skywars;
    public ServerPingListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleServerPing(ServerListPingEvent event) {
        event.setMotd(GameData.getTeamAmount() + "x" + GameData.getTeamSize());

        if (GameState.state == GameState.LOBBY) {
            event.setMaxPlayers(GameData.getTeamAmount() * GameData.getTeamSize());
        } else {
            event.setMaxPlayers((GameData.getTeamAmount() * GameData.getTeamSize()) + 20);
        }
    }

}
