package de.zayon.skywars.countdowns;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.zayonapi.ZayonAPI;
import de.zayon.zayonapi.items.Items;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class IngameCountdown {

    @Getter public static int counter = 0;
    private final Skywars skywars;
    public IngameCountdown(Skywars skywars) {
        this.skywars = skywars;
    }
    public static int ingameCounter = 0;

    public static void ingameCountdown() {

        Bukkit.getScheduler().cancelTasks(Skywars.getSkywars());
        Bukkit.getOnlinePlayers().forEach(Skywars.getSkywars().getScoreboardManager()::setUserScoreboard);
        ZayonAPI.getZayonAPI().getTeamAPI().removeEmptyTeams();

        ingameCounter = Bukkit.getScheduler().scheduleSyncRepeatingTask(Skywars.getSkywars(), () -> {
            if(counter == 20) {
                Bukkit.broadcastMessage(StringData.getPrefix() + "Die Schutzzeit endet in " + StringData.getHighlightColor() + "10 Sekunden§7.");
            } else if (counter == 30) {
                Bukkit.broadcastMessage(StringData.getPrefix() + "Die Schutzzeit endet " + StringData.getHighlightColor() + "jetzt§7.");
            }
            else if(counter == GameData.getMaxGameTime()) {
                Bukkit.broadcastMessage(StringData.getPrefix() + "Das Spiel ist zuende, es gibt keinen Gewinner.");
                GameState.state = GameState.END;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.teleport(GameData.getLobbyLocation());
                    player.getInventory().clear();
                    player.getInventory().setItem(8, Items.createItem(Material.HEART_OF_THE_SEA, "§7Zurück zur Lobby", 1));
                }
                EndingCoutdown.closeCountdown();
            }
            counter++;
        }, 20L, 20L);
    }
}
