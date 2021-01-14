package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.zayonapi.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    private final Skywars skywars;
    public PlayerJoinListener(Skywars skywars) {this.skywars = skywars;}

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();
        player.setHealthScale(20D);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        event.setJoinMessage("");

        this.skywars.getUserFactory().createUser(player);
        Bukkit.getOnlinePlayers().forEach(this.skywars.getScoreboardManager()::setUserScoreboard);

        if (GameState.state == GameState.LOBBY) {

//            player.teleport(GameData.getLobbyLocation());
            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + player.getDisplayName() + " §7hat das Spiel betreten.");
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA,"§7Zurück zur Lobby", 1));
            player.getInventory().setItem(0, Items.createItem(Material.TOTEM_OF_UNDYING, "§7Teamauswahl", 1));
            player.getInventory().setItem(1, Items.createItem(Material.CHEST,  "§7Kits", 1));
            player.updateInventory();

            ArrayList playerList = GameData.getIngame();
            playerList.add(player);
            GameData.setIngame(playerList);

            if (player.hasPermission("skywars.start") || player.hasPermission("rang.vip+")) {
                player.getInventory().setItem(1, Items.createItem(Material.DIAMOND, "§7Starte das Spiel", 1));
            }
            if (Bukkit.getOnlinePlayers().size() == 2) {
                this.skywars.getLobbyCountdown().startLobbyCountdown(false);
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(Skywars.getSkywars(), player);
            }
        }
    }

    @EventHandler
    public void handleSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        Location location = GameData.getLobbyLocation();
        event.setSpawnLocation(location);
    }
}
