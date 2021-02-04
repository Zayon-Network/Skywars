package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.countdowns.EndingCoutdown;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.zayonapi.PointsAPI.PointsAPI;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.TeamAPI.TeamAPI;
import de.zayon.zayonapi.ZayonAPI;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class DeathListener implements Listener {

    private Skywars skywars;
    @Getter private HashMap<Player, Integer> killCount = new HashMap<Player, Integer>();

    public DeathListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(this.skywars, () -> {
            player.sendMessage(StringData.getPrefix() + "Du hast §c200 §7Punkte erhalten.");
            player.sendTitle("", "§9Du bist Gestorben!", 40, 120, 60);
            event.setDroppedExp(0);

            if (GameState.state == GameState.INGAME) {
                Bukkit.getScheduler().runTask(this.skywars, () -> {
                    PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
                    ((CraftPlayer) player).getHandle().playerConnection.a(packet);
                    player.setGameMode(GameMode.SPECTATOR);
                });
                GameData.removePlayerFromIngame(player);


                if (event.getEntity().getKiller() instanceof Player) {
                    Player killer = event.getEntity().getKiller().getPlayer();

                    this.killCount.replace(killer, killCount.get(killer) + 1);
                    ZayonAPI.getZayonAPI().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 50);
                    ZayonAPI.getZayonAPI().getPointsAPI().updatePoints(player, PointsAPI.UpdateType.ADD, 250);
                    killer.sendMessage(StringData.getPrefix() + "§7Du hast  " + StringData.getHighlightColor() + "250 Punkte§7 erhalten!");
                    player.sendMessage(StringData.getPrefix() + "§7Du hast §9 " + StringData.getHighlightColor() + "50 Punkte§7 erhalten!");
                    event.setDeathMessage(StringData.getPrefix() + "§7Der Spieler " + StringData.getHighlightColor() + player.getDisplayName() + " §7wurde von " + StringData.getHighlightColor() + killer.getDisplayName() + " §7getötet!");


                    Bukkit.getScheduler().runTask(this.skywars, () -> {
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 5));
                    });
                } else {
                    event.setDeathMessage(StringData.getPrefix() + "§7Der Spieler " + StringData.getHighlightColor() + player.getDisplayName() + " §7hat Selbstmord begangen!");
                    Bukkit.getScheduler().runTaskLater(this.skywars, () -> {
                        player.teleport(new Location(Bukkit.getWorld("WLobby"), 152.5, 52, 148.5, 60, 0));
                    }, 15);
                }

                Team team = GameData.getTeamCache().get(player);
                team.removePlayer(player);
                GameData.getTeamCache().remove(player);
                if(team.getRegisteredPlayers().isEmpty()) {
                    ZayonAPI.getZayonAPI().getTeamAPI().removeTeam(team);
                }

                if (ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams().size() == 1) {
                    EndingCoutdown.teamWin(GameData.getTeamCache().get(GameData.getIngame().get(0)));
                }
            }
        });
    }
}
