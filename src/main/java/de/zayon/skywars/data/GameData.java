package de.zayon.skywars.data;

import de.zayon.skywars.Skywars;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.items.Items;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameData {

    @Getter private static Integer teamSize = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.teamSize", 2);
    @Getter private static Integer teamAmount = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.teamAmount", 9);
    @Getter private static Integer startTime = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.startTime", 60);
    @Getter private static Integer maxGameTime = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.maxGameTime", 7200);

    @Getter private static String mapName = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.map.name", "MapName");
    @Getter private static String mapBuilder = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.map.builder", "Builder");
    @Getter private static Location lobbyLocation = new Location(Bukkit.getWorld("WLobby"), 4.5, 44, -71.5, 0, 0);

    @Getter @Setter private static ArrayList<Material> itemsToFind = new ArrayList<Material>();
    @Getter @Setter private static ArrayList<Player> ingame = new ArrayList<Player>();
    public static void removePlayerFromIngame(Player player) {ingame.remove(player);}
    @Getter private static HashMap<Player, Team> teamCache = new HashMap<Player, Team>();
    @Getter private static ArrayList<ItemStack> itemList = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.itempool", new ArrayList<ItemStack>(
            Arrays.asList(
                    Items.createItem(Material.BRICK, "", 32)
            )
    ));

}
