package de.nehlen.skywars.countdowns;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameData;
import de.nehlen.skywars.data.GameState;
import de.nehlen.skywars.data.StringData;
import de.nehlen.skywars.factory.UserFactory;
import de.nehlen.skywars.sidebar.util.UtilFunctions;
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
            counter = 15;
        }

        GameData.loadItems();
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Skywars.getSkywars(), () -> {

            if (counter >= 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    UtilFunctions.ActionBar(p, "§7Noch §f" + counter + " §7Sekunden...");
                }

                if (counter == 60 ||counter == 30 ||counter == 15 || counter == 10) {
                    Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet in " + StringData.getHighlightColor() + counter + " §7Sekunden!");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(counter + " §7Sek.", "§7bis zum Start...", 5, 20, 5);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                    }

                } else if (counter == 5) {

                    Bukkit.getScheduler().runTaskAsynchronously(Skywars.getSkywars(), () -> {
                        for (Team team : Gameapi.getGameapi().getTeamAPI().getRegisteredTeams()) {
                            Location block = Skywars.getSkywars().getLocationConfig().getLocation("config.location.team." + Gameapi.getGameapi().getTeamAPI().getRegisteredTeams().indexOf(team));
                            team.addToMemory("spawnLoc", block);
                        }
                    });

                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel wird gestartet.");
                    Bukkit.broadcastMessage(StringData.getPrefix() + "§7Spieler Online: " + StringData.getHighlightColor() + Bukkit.getOnlinePlayers().size() + "§7/" + StringData.getHighlightColor() + (GameData.getTeamAmount() * GameData.getTeamSize()) + "§7.");
                    Bukkit.broadcastMessage(StringData.getPrefix() + "§7Map: " + StringData.getHighlightColor() + GameData.getMapName() + " §7gebaut von " + StringData.getHighlightColor() + GameData.getMapBuilder() + "§7.");
                    Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet in " + StringData.getHighlightColor() + counter + " §7Sekunden!");
                    Bukkit.broadcastMessage("");


                    Bukkit.getOnlinePlayers().forEach(players -> {
                        players.sendTitle("§f" + counter + " §7Sek.", "§7bis zum Start...", 5, 20, 5);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10.0F, 1.0F);
                    });
                } else if (counter == 0) {

                    if (Bukkit.getOnlinePlayers().size() < (GameData.getTeamSize()+1)) {
                        counter = 60;
                        Bukkit.broadcastMessage(StringData.getPrefix() + "Es sind nicht genug Spieler Online. Der Countdown startet neu.");
                    } else {

                        Bukkit.broadcastMessage(StringData.getPrefix() + "§7Das Spiel startet " + StringData.getHighlightColor() + "jetzt§!");

//                        new BukkitRunnable() {
//                            @Override
//                            public void run() {

                                //SET WORLD SETTINGS
                                World world = Bukkit.getWorld("world");
                                world.getWorldBorder().setCenter(new Location(world, 0, 0, 0));
                                world.setDifficulty(Difficulty.EASY);
                                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);

                                // SET CURRENT PLAYERLIST TO GAMEDATA
                                // SET PLAYER IN TEAMS
                                ArrayList<Player> playerList = new ArrayList<>();
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                                    playerList.add(player);
                                    Skywars.getSkywars().getUserFactory().updateGames(player, UserFactory.UpdateType.ADD, 1);
                                    Skywars.getSkywars().getScoreboardManager().removeUserScoreboard(player);
                                    if (!GameData.getTeamCache().containsKey(player)) {
                                        Team team = Gameapi.getGameapi().getTeamAPI().addToLowestTeam(player);
                                        GameData.getTeamCache().put(player, team);
                                    }
                                });

                                GameData.setIngame(playerList);

                                //TELEPORT PLAYER TO WORLD
                                Gameapi.getGameapi().getTeamAPI().getRegisteredTeams().forEach(team -> {
                                    Location loc = (Location) team.getMemory().get("spawnLoc");
                                    team.getRegisteredPlayers().forEach(player -> {
                                        UtilFunctions.ActionBar(player, "§7Du wirst teleportiert...");
                                        player.getInventory().clear();
                                        player.setGameMode(GameMode.SURVIVAL);
//                                        Bukkit.getScheduler().runTask(Skywars.getSkywars(), () -> player.teleport(loc));
                                        player.teleport(loc);
//                                        player.setDisplayName(GameData.getTeamCache().get(player).getTeamColor() + player.getDisplayName());
                                    });

                                });
                            Skywars.getSkywars().getKitManager().giveKitToPlayers();
//                            }
//                        }.runTask(Skywars.getSkywars());

                        //SET GAME STATUS TO INGAME
                        BukkitCloudNetHelper.changeToIngame();
                        Bukkit.getScheduler().cancelTask(scheduler);
                        IngameCountdown.ingameCountdown();
                        GameState.state = GameState.INGAME;
                    }
                }
            }

            counter--;
        }, 20L, 20L);
    }
}