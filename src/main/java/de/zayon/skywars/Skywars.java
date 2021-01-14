package de.zayon.skywars;

import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.zayon.skywars.commands.SetspawnCommand;
import de.zayon.skywars.commands.StartCommand;
import de.zayon.skywars.countdowns.EndingCoutdown;
import de.zayon.skywars.countdowns.IngameCountdown;
import de.zayon.skywars.countdowns.LobbyCountdown;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.factory.UserFactory;
import de.zayon.skywars.listener.*;
import de.zayon.skywars.manager.GroupManager;
import de.zayon.skywars.manager.KitManager;
import de.zayon.skywars.manager.ScoreboardManager;
import de.zayon.skywars.sidebar.SidebarCache;
import de.zayon.skywars.sidebar.util.DatabaseLib;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.ZayonAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Skywars extends JavaPlugin {

    @Getter private static Skywars skywars;
    @Getter private SpigotConfig generalConfig;
    @Getter private SpigotConfig locationConfig;
    @Getter private SidebarCache sidebarCache;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private GroupManager groupManager;
    @Getter private KitManager kitManager;
    @Getter private DatabaseLib databaseLib;
    @Getter private UserFactory userFactory;

    @Getter private SetspawnCommand setspawnCommand;
    @Getter private StartCommand startCommand;

    @Getter private AsyncPlayerChatListener asyncPlayerChatListener;
    @Getter private BuildListener buildListener;
    @Getter private DamageListener damageListener;
    @Getter private DeathListener deathListener;
    @Getter private InteractListener interactListener;
    @Getter private PlayerJoinListener playerJoinListener;
    @Getter private PlayerQuitListener playerQuitListener;
    @Getter private ProtectionListener protectionListener;
    @Getter private ServerPingListener serverPingListener;
    @Getter private WeatherChangeListener weatherChangeListener;


    @Getter private LobbyCountdown lobbyCountdown;
    @Getter private EndingCoutdown endingCoutdown;
    @Getter private IngameCountdown ingameCountdown;

    @Override
    public void onEnable() {
        skywars = this;

        this.generalConfig = ConfigFactory.create(new File(getDataFolder(), "general_settings.yml"), SpigotConfig.class);
        this.locationConfig = ConfigFactory.create(new File(getDataFolder(), "location_settings.yml"), SpigotConfig.class);

        this.sidebarCache = new SidebarCache();
        this.scoreboardManager = new ScoreboardManager(this);
        this.groupManager = new GroupManager();
        this.kitManager = new KitManager();
        this.databaseLib = new DatabaseLib(this);
        this.userFactory = new UserFactory(this);
        this.setspawnCommand = new SetspawnCommand(this);
        this.startCommand = new StartCommand(this);
        this.asyncPlayerChatListener = new AsyncPlayerChatListener(this);
        this.buildListener = new BuildListener(this);
        this.damageListener = new DamageListener(this);
        this.deathListener = new DeathListener(this);
        this.interactListener = new InteractListener(this);
        this.playerJoinListener = new PlayerJoinListener(this);
        this.playerQuitListener = new PlayerQuitListener(this);
        this.protectionListener = new ProtectionListener(this);
        this.serverPingListener = new ServerPingListener(this);
        this.weatherChangeListener = new WeatherChangeListener(this);
        this.lobbyCountdown = new LobbyCountdown(this);
        this.endingCoutdown = new EndingCoutdown(this);
        this.ingameCountdown = new IngameCountdown(this);


        Bukkit.getServer().getPluginManager().registerEvents(asyncPlayerChatListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(buildListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(damageListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(deathListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(interactListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerJoinListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(playerQuitListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(protectionListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(serverPingListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(weatherChangeListener, this);
        getCommand("start").setExecutor(startCommand);
        getCommand("setspawn").setExecutor(setspawnCommand);
    }

    public static void loadTeams() {
        ArrayList<ChatColor> colors = new ArrayList<ChatColor>(
                Arrays.asList(
                        ChatColor.RED,
                        ChatColor.BLUE,
                        ChatColor.GREEN,
                        ChatColor.YELLOW,
                        ChatColor.LIGHT_PURPLE,
                        ChatColor.AQUA,
                        ChatColor.DARK_GREEN,
                        ChatColor.GOLD,
                        ChatColor.DARK_AQUA,
                        ChatColor.DARK_RED,
                        ChatColor.DARK_BLUE));


        for (int i = 0; i < GameData.getTeamAmount(); i++) {
            Team team = new Team();
            team.setMaxTeamSize(GameData.getTeamSize());
            team.setTeamColor(colors.get(i));
            team.setTeamName(colors.get(i) + "Team-" + (i+1));

            ZayonAPI.getZayonAPI().getTeamAPI().addTeam(team);
        }
    }
}
