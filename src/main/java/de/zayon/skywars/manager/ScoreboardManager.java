package de.zayon.skywars.manager;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.countdowns.IngameCountdown;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.ScoreboardData;
import de.zayon.skywars.listener.DeathListener;
import de.zayon.skywars.sidebar.Sidebar;
import de.zayon.skywars.sidebar.SidebarCache;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;

public class ScoreboardManager {
    private final Skywars skywars;

    private final HashMap<Player, BukkitTask> scoreboardTaskMap = new HashMap<>();

    private final int updateInterval = 1;

    public ScoreboardManager(Skywars skywars) {
        this.skywars = skywars;
    }

    public void setUserScoreboard(final Player player) {
        if (!this.scoreboardTaskMap.containsKey(player))
            this.scoreboardTaskMap.put(player, (new BukkitRunnable() {
                int counter = 0;
                int sec = 0;

                public void run() {
                    ScoreboardManager.this.setScoreboardContent(player, this.counter);
                    if (sec >= 15) {
                        this.counter = ++this.counter % (ScoreboardData.values()).length;
                        sec = 0;
                    }
                    sec++;
                }
            }).runTaskTimer((Plugin) this.skywars, 0L, 20L));
    }

    public void removeUserScoreboard(Player player) {
        if (this.scoreboardTaskMap.containsKey(player)) {
            ((BukkitTask) this.scoreboardTaskMap.get(player)).cancel();
            this.scoreboardTaskMap.remove(player);
        }
    }

    private void setScoreboardContent(Player player, int pageNumber) {
        ScoreboardData scoreboardData = ScoreboardData.values()[pageNumber];
        this.skywars.getSidebarCache();
        Sidebar sidebar = SidebarCache.getUniqueCachedSidebar(player);
        sidebar.setDisplayName(scoreboardData.getDisplayName());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        sidebar.setLines(scoreboardData.getLines(),
                "%kills%", this.skywars.getDeathListener().getKillCount().get(player),
                "%gamestatus%", GameState.state.toString(),
                "%time%", getTime(IngameCountdown.getCounter()),
                "%kit%", getKit(player),
                "%team%", getTeam(player)
        );
    }

    private String getTime(Integer seconds) {
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
        String time = timeOfDay.toString();
        return time;
    }

    private String getTeam(Player player) {
        if(GameData.getTeamCache().containsKey(player)) {
            return GameData.getTeamCache().get(player).getTeamName();
        } else {
            return "Kein Team";
        }
    }

    private String getKit(Player player) {
        String kitName = this.skywars.getKitManager().getCurrentKit(player).toString().toLowerCase(Locale.GERMANY);
        return kitName.substring(0, 1).toUpperCase() + kitName.substring(1).replace("_", " ")
                .replaceAll("oe", "ö")
                .replaceAll("ae", "ä")
                .replaceAll("ue", "ü")
                .toLowerCase(Locale.GERMANY);
    }

}
