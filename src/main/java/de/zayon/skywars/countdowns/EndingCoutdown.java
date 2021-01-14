package de.zayon.skywars.countdowns;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.skywars.factory.UserFactory;
import de.zayon.zayonapi.PointsAPI.PointsAPI;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.ZayonAPI;
import de.zayon.zayonapi.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EndingCoutdown {

    static int counter = 20;
    private final Skywars skywars;

    public EndingCoutdown(Skywars skywars) {
        this.skywars = skywars;
    }

    public static void teamWin(Team t) {

        GameState.state = GameState.END;
        Bukkit.getScheduler().cancelTask(IngameCountdown.ingameCounter);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(StringData.getHighlightColor() + "Team-" + (t.getTeamName()), " §7hat das Spiel Gewonnen", 20, 60, 0);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 0);
            player.spawnParticle(Particle.TOTEM, player.getLocation().add(0,1,0), 250);
        }

        Bukkit.getScheduler().runTaskLater(Skywars.getSkywars(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(GameData.getLobbyLocation());
                player.getInventory().clear();
                player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, "§7Zurück zur Lobby", 1));
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(StringData.getPrefix() + StringData.getHighlightColor() + "Team-" + t.getTeamName() + " §7hat das Spiel Gewonnen");
            Bukkit.broadcastMessage(StringData.getPrefix() + "Zu diesem Team gehören:");
            for (Player player : t.getRegisteredPlayers()) {
                Bukkit.broadcastMessage("§7- " + player.getDisplayName());
                //TODO Add coins

                ZayonAPI.getZayonAPI().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 250);
                player.sendMessage(StringData.getPrefix() + "Du hast §c250 §7Punkte erhalten.");

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
                                BridgePlayerManager.getInstance().proxySendPlayer(BridgePlayerManager.getInstance().getOnlinePlayer(player.getUniqueId()), "Lobby-1");
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
