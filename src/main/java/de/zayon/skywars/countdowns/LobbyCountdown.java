package de.zayon.skywars.countdowns;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.skywars.factory.UserFactory;
import de.zayon.skywars.sidebar.util.UtilFunctions;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.TeamAPI.TeamAPI;
import de.zayon.zayonapi.ZayonAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LobbyCountdown {

    private final Skywars skywars;

    public LobbyCountdown(Skywars skywars) {
        this.skywars = skywars;
    }

    public static int counter = GameData.getStartTime();
    public static int scheduler = 0;

    public static void startLobbyCountdown(boolean fast) {
        if (fast) {
            counter = 10;
        }

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZayonAPI.getZayonAPI(), new Runnable() {
            @Override
            public void run() {


                if (counter >= 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        UtilFunctions.ActionBar(p, "§7Noch §f" + counter + " §7Sekunden...");
                    }

                    if (counter == 15 || counter == 10) {
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet in " + StringData.getHighlightColor() + counter + " §7Sekunden!");
                        for (Player players : Bukkit.getOnlinePlayers()) {

                            players.sendTitle(counter + " §7Sek.", "§7bis zum Start...", 5, 20, 5);
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                        }

                    } else if (counter == 5) {
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel wird gestartet.");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Spieler Online: " + StringData.getHighlightColor() + Bukkit.getOnlinePlayers().size() + "§7/" + StringData.getHighlightColor() + (GameData.getTeamAmount() * GameData.getTeamSize()) + "§7.");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Map: " + StringData.getHighlightColor() + GameData.getMapName() + " §7gebaut von " + StringData.getHighlightColor() + GameData.getMapBuilder() + "§7.");
                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet in " + StringData.getHighlightColor() + counter + " §7Sekunden!");
                        Bukkit.broadcastMessage("");


                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.sendTitle("§f" + counter + " §7Sek.", "§7bis zum Start...", 5, 20, 5);
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                        }
                    } else if (counter == 0) {

                        if (Bukkit.getOnlinePlayers().size() < 2) {
                            counter = 60;
                            Bukkit.broadcastMessage(StringData.getPrefix() + "Es sind nicht genug Spieler Online. Der Countdown startet neu.");
                        } else {

                            Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet " + StringData.getHighlightColor() + "jetzt§!");

                            //SET WORLD AND LOCATION SETTINGS
                            Bukkit.getScheduler().runTaskAsynchronously(Skywars.getSkywars(), () -> {
                                //SET WORLD SETTINGS
                                World world = Bukkit.getWorld("world");
                                world.getWorldBorder().setCenter(new Location(world, 0, 0, 0));
//                                world.getWorldBorder().setSize(GameData.getWorldSize());
                                world.setDifficulty(Difficulty.EASY);
                                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                                world.setStorm(false);
                            });

                            // SET CURRENT PLAYERLIST TO GAMEDATA
                            // SET PLAYER IN TEAMS
                            ArrayList<Player> playerList = new ArrayList<>();
                            Bukkit.getScheduler().runTaskAsynchronously(Skywars.getSkywars(), () -> {
                                for (final Player player : Bukkit.getOnlinePlayers()) {
                                    if (!GameData.getTeamCache().containsKey(player)) {
                                        ZayonAPI.getZayonAPI().getTeamAPI().addPlayerRandom(player);
                                    }

                                    Skywars.getSkywars().getUserFactory().updateGames(player, UserFactory.UpdateType.ADD, 1);
                                    Skywars.getSkywars().getScoreboardManager().removeUserScoreboard(player);

                                    playerList.add(player);
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                                }
                            });
                            GameData.setIngame(playerList);


                            //TELEPORT
                            Bukkit.getScheduler().runTaskAsynchronously(Skywars.getSkywars(), () -> {
                                for (Player player : GameData.getIngame()) {
                                    Location location = Skywars.getSkywars().getLocationConfig().getLocation("config.location.team." +
                                            ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams().indexOf(GameData.getTeamCache().get(player)));
                                    player.getInventory().clear();
                                    player.setGameMode(GameMode.SURVIVAL);
                                    //TODO GIVE KIT TO PLAYER

                                    Bukkit.getScheduler().runTask(Skywars.getSkywars(), () -> {
                                        player.teleport(location);

                                    });
                                }
                                Skywars.getSkywars().getKitManager().giveKitToPlayers();
                            });


                            //SET GAME STATUS TO INGAME
                            BukkitCloudNetHelper.changeToIngame();
                            Bukkit.getScheduler().cancelTask(scheduler);
                            Skywars.getSkywars().getIngameCountdown().ingameCountdown();
                            GameState.state = GameState.INGAME;
                        }
                    }
                }

                counter--;
            }
        }, 20L, 20L);
    }
}



