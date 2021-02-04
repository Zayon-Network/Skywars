package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.countdowns.EndingCoutdown;
import de.zayon.skywars.countdowns.LobbyCountdown;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.ZayonAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Skywars skywars;
    public PlayerQuitListener(Skywars skywars) {this.skywars = skywars;}

    @EventHandler
    public void handleLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (GameState.state == GameState.LOBBY) {
            event.setQuitMessage(StringData.getPrefix() + StringData.getHighlightColor() + event.getPlayer().getName() + " §7hat das Spiel verlassen.");
            if(Bukkit.getOnlinePlayers().size() <= 2) {
                Bukkit.broadcastMessage(StringData.getPrefix() + "§7Der Countdown wurde gestopt weil zu wenig Spieler drin sind.");
                Bukkit.getScheduler().cancelTask(LobbyCountdown.scheduler);
            }
        } else if (GameState.state == GameState.INGAME && GameData.getIngame().contains(player)) {
            event.setQuitMessage(StringData.getPrefix() + StringData.getHighlightColor() + event.getPlayer().getName() + " §7hat das Spiel verlassen.");

            Team team = GameData.getTeamCache().get(player);
            team.removePlayer(player);
            GameData.getTeamCache().remove(player);
            if(team.getRegisteredPlayers().isEmpty()) {
                ZayonAPI.getZayonAPI().getTeamAPI().removeTeam(team);
            }

            if (ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams().size() == 1) {
                EndingCoutdown.teamWin(GameData.getTeamCache().get(GameData.getIngame().get(0)));
            }
        } else {
            event.setQuitMessage("");
        }
    }
}
