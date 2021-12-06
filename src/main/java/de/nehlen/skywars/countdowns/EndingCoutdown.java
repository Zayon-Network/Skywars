package de.nehlen.skywars.countdowns;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.ItemsAPI.Items;
import de.nehlen.gameapi.PointsAPI.PointsAPI;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameState;
import de.nehlen.skywars.data.StringData;
import de.nehlen.skywars.factory.UserFactory;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class EndingCoutdown {

    static int counter = 20;
    private final Skywars skywars;
    static private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public EndingCoutdown(Skywars skywars) {
        this.skywars = skywars;
    }

    public static void teamWin(Team t) {

        GameState.state = GameState.END;
        Bukkit.getScheduler().cancelTask(IngameCountdown.ingameCounter);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(StringData.getHighlightColor() + (t.getTeamName()), " §7hat das Spiel Gewonnen", 20, 60, 0);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 0);
            player.spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), 250);
        }

        Bukkit.getScheduler().runTaskLater(Skywars.getSkywars(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(new Location(Bukkit.getWorld("WLobby"), 152.5, 52, 148.5, 60, 0));
                player.getInventory().clear();
                player.setTotalExperience(0);
                player.setHealth(20L);
                player.setFoodLevel(20);
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, "§7Zurück zur Lobby", 1));
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + t.getTeamName() + " §7hat das Spiel Gewonnen");
            Bukkit.broadcastMessage(StringData.getPrefix() + "Zu diesem Team gehören:");
            for (Player player : t.getRegisteredPlayers()) {
                Bukkit.broadcastMessage("§7- " + player.getDisplayName());
                //TODO Add coins

                Gameapi.getGameapi().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 350);
                player.sendMessage(StringData.getPrefix() + "Du hast " + StringData.getHighlightColor() + "350 §7Punkte erhalten.");

                Skywars.getSkywars().getUserFactory().updateWins(player, UserFactory.UpdateType.ADD, 1);
            }
            Bukkit.broadcastMessage("");
            Bukkit.getScheduler().cancelTasks(Skywars.getSkywars());
            closeCountdown();
        }, 100);
    }

    public static void closeCountdown() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Skywars.getSkywars(), new Runnable() {
            @Override
            public void run() {

                if (counter <= 10) {
                    Bukkit.broadcastMessage(StringData.getPrefix() + "Der Server startet in " + StringData.getHighlightColor() + counter + " Sekunden §7neu.");
                }
                if (counter == 0) {
                    Bukkit.getScheduler().runTaskAsynchronously(Skywars.getSkywars(), new Runnable() {
                        @Override
                        public void run() {
                            for (final Player player : Bukkit.getOnlinePlayers()) {
                                playerManager.getPlayerExecutor(playerManager.getOnlinePlayer(player.getUniqueId())).connectToGroup("Lobby", ServerSelectorType.RANDOM);
                            }
                        }
                    });

                } else if (counter == -3) {
                    Bukkit.getServer().shutdown();
                }
                counter--;
            }
        }, 20, 20);
    }
}
