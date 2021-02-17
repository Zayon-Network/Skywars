package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.countdowns.LobbyCountdown;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.skywars.manager.KitManager;
import de.zayon.zayonapi.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        player.setTotalExperience(0);
        player.setGameMode(GameMode.ADVENTURE);
        event.setJoinMessage("");

        this.skywars.getUserFactory().createUser(player);
        this.skywars.getKitManager().setKit(player, KitManager.Kit.STARTER);
        Bukkit.getOnlinePlayers().forEach(this.skywars.getScoreboardManager()::setUserScoreboard);
        Skywars.getSkywars().getDeathListener().getKillCount().put(player, 0);

        if (GameState.state == GameState.LOBBY) {
            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + player.getDisplayName() + " §7hat das Spiel betreten.");
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA,"§7Zurück zur Lobby", 1));
            player.getInventory().setItem(0, Items.createItem(Material.CHEST,  "§7Kitauswahl", 1));
            player.getInventory().setItem(1, Items.createItem(Material.TOTEM_OF_UNDYING, "§7Teamauswahl", 1));
            player.updateInventory();

            ArrayList<Player> playerList = GameData.getIngame();
            playerList.add(player);
            GameData.setIngame(playerList);

            if (Bukkit.getOnlinePlayers().size() == (GameData.getTeamSize()+1)) {
                LobbyCountdown.startLobbyCountdown(false);
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(Skywars.getSkywars(), player);
            }
        }  else if (GameState.state == GameState.INGAME) {

            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(true);
            player.setAllowFlight(true);
            player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA,"§7Zurück zur Lobby", 1));
            player.getInventory().setItem(0, Items.createItem(Material.COMPASS, "§7Spieler", 1));

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.hidePlayer(this.skywars, player);
            }
            player.sendMessage("");
            player.sendMessage("§7Diese Runde kannst du nur noch Zuschauen!");
            player.sendMessage("§7Nutze um zu den Kompass um dich zu einem Spieler zu teleportieren.");
        }
    }

    @EventHandler
    public void handleSpawn(PlayerSpawnLocationEvent event) {
//        Location location = GameData.getLobbyLocation();
        event.setSpawnLocation(new Location(Bukkit.getWorld("WLobby"), 152.5, 52, 148.5, 60, 0));
    }
}
