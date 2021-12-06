package de.nehlen.skywars.listener;

import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.countdowns.EndingCoutdown;
import de.nehlen.skywars.countdowns.LobbyCountdown;
import de.nehlen.skywars.data.GameData;
import de.nehlen.skywars.data.GameState;
import de.nehlen.skywars.data.StringData;
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
                Gameapi.getGameapi().getTeamAPI().removeTeam(team);
            }

            if (Gameapi.getGameapi().getTeamAPI().getRegisteredTeams().size() == 1) {
                EndingCoutdown.teamWin(GameData.getTeamCache().get(GameData.getIngame().get(0)));
            }
        } else {
            event.setQuitMessage("");
        }
    }
}
