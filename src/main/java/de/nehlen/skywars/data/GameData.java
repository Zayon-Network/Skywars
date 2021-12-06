package de.nehlen.skywars.data;

import de.nehlen.gameapi.ItemsAPI.Items;
import de.nehlen.gameapi.TeamAPI.Team;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.sidebar.util.UtilFunctions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameData {

    @Getter
    private static final Integer teamSize = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.teamSize", 2);
    @Getter
    private static final Integer teamAmount = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.teamAmount", 9);
    @Getter
    private static final Integer startTime = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.startTime", 60);
    @Getter
    private static final Integer maxGameTime = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.game.maxGameTime", 7200);

    @Getter
    private static final String mapName = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.map.name", "MapName");
    @Getter
    private static final String mapBuilder = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.map.builder", "Builder");
    @Getter
    private static final Location lobbyLocation = new Location(Bukkit.getWorld("WLobby"), 152.5, 52, 148.5, 60, 0);

    @Getter
    @Setter
    private static ArrayList<Player> ingame = new ArrayList<>();

    public static void removePlayerFromIngame(Player player) {
        ingame.remove(player);
    }

    @Getter
    private static HashMap<Player, Team> teamCache = new HashMap<>();
    @Getter
    @Setter
    private static List<ItemStack> itemList = new ArrayList<>();

    public static void loadItems() {
        Bukkit.getScheduler().runTaskAsynchronously(Skywars.getSkywars(), () -> {
            for (int i = 0; i < 10; i++) {
                itemList.add(Items.createItem(Material.BRICKS, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.BIRCH_PLANKS, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.OAK_PLANKS, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.ACACIA_PLANKS, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.DIORITE, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.GRANITE, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.TERRACOTTA, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.RED_MUSHROOM_BLOCK, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.STONE, "", UtilFunctions.getRandomNumberInRange(16, 64)));
                itemList.add(Items.createItem(Material.GLASS, "", UtilFunctions.getRandomNumberInRange(16, 32)));
                itemList.add(Items.createItem(Material.BONE_BLOCK, "", UtilFunctions.getRandomNumberInRange(16, 32)));
                itemList.add(Items.createItem(Material.SNOWBALL, "", UtilFunctions.getRandomNumberInRange(1, 5)));
                itemList.add(Items.createItem(Material.EGG, "", UtilFunctions.getRandomNumberInRange(1, 5)));
                itemList.add(Items.createItem(Material.LAPIS_LAZULI, "", UtilFunctions.getRandomNumberInRange(1, 6)));
                itemList.add(Items.createItem(Material.FLINT, "", UtilFunctions.getRandomNumberInRange(1, 3)));
                itemList.add(Items.createItem(Material.STICK, "", UtilFunctions.getRandomNumberInRange(1, 3)));
                itemList.add(Items.createItem(Material.EXPERIENCE_BOTTLE, "", UtilFunctions.getRandomNumberInRange(3, 8)));
                itemList.add(Items.createItem(Material.IRON_INGOT, "", UtilFunctions.getRandomNumberInRange(3, 8)));
                itemList.add(Items.createItem(Material.ENDER_PEARL, "", UtilFunctions.getRandomNumberInRange(1, 2)));
                itemList.add(Items.createItem(Material.TNT, "", UtilFunctions.getRandomNumberInRange(2, 6)));
                itemList.add(Items.createItem(Material.GOLDEN_APPLE, "", UtilFunctions.getRandomNumberInRange(1, 3)));
                itemList.add(Items.createItem(Material.DIAMOND, "", UtilFunctions.getRandomNumberInRange(1, 7)));
                itemList.add(Items.createItem(Material.BEEF, "", UtilFunctions.getRandomNumberInRange(3, 15)));
                itemList.add(Items.createItem(Material.PUMPKIN_PIE, "", UtilFunctions.getRandomNumberInRange(3, 5)));
                itemList.add(Items.createItem(Material.COBWEB, "", UtilFunctions.getRandomNumberInRange(4, 8)));
                itemList.add(Items.createItem(Material.COOKED_BEEF, "", UtilFunctions.getRandomNumberInRange(3, 15)));
            }

            for (int i = 0; i < 5; i++) {
                itemList.add(Items.createItem(Material.IRON_SWORD, "", 1));
                itemList.add(Items.createItem(Material.ENCHANTING_TABLE, "", 1));
                itemList.add(Items.createItem(Material.LAVA_BUCKET, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_HELMET, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_LEGGINGS, "", 1));
                itemList.add(Items.createItem(Material.DIAMOND_BOOTS, "", 1));
                itemList.add(Items.createItem(Material.DIAMOND_CHESTPLATE, "", 1));
                itemList.add(Items.createItem(Material.DIAMOND_HELMET, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_CHESTPLATE, "", 1));
                itemList.add(Items.createItem(Material.DIAMOND_PICKAXE, "", 1));
                itemList.add(Items.createItem(Material.IRON_PICKAXE, "", 1));
                itemList.add(Items.createItem(Material.LEATHER_BOOTS, "", 1));
                itemList.add(Items.createItem(Material.CAKE, "", 1));
                itemList.add(Items.createItem(Material.IRON_HELMET, "", 1));
                itemList.add(Items.createItem(Material.LEATHER_HELMET, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_PICKAXE, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_BOOTS, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_AXE, "", 1));
                itemList.add(Items.createItem(Material.CHAINMAIL_CHESTPLATE, "", 1));
                itemList.add(Items.createItem(Material.CHAINMAIL_HELMET, "", 1));
                itemList.add(Items.createItem(Material.CHAINMAIL_LEGGINGS, "", 1));
                itemList.add(Items.createItem(Material.CHAINMAIL_BOOTS, "", 1));
                itemList.add(Items.createItem(Material.DIAMOND_AXE, "", 1));
                itemList.add(Items.createItem(Material.GOLDEN_SWORD, "", 1));
                itemList.add(Items.createItem(Material.LEATHER_CHESTPLATE, "", 1));
                itemList.add(Items.createItem(Material.IRON_CHESTPLATE, "", 1));
                itemList.add(Items.createItem(Material.IRON_LEGGINGS, "", 1));
                itemList.add(Items.createItem(Material.IRON_BOOTS, "", 1));
                itemList.add(Items.createItem(Material.LEATHER_LEGGINGS, "", 1));
                itemList.add(Items.createItem(Material.DIAMOND_LEGGINGS, "", 1));
                itemList.add(Items.createItem(Material.COMPASS, "", 1));
                itemList.add(Items.createItem(Material.STONE_AXE, "", 1));
                itemList.add(Items.createItem(Material.STONE_SWORD, "", 1));
                itemList.add(Items.createItem(Material.WATER_BUCKET, "", 1));
                itemList.add(Items.createItem(Material.WOODEN_SWORD, "", 1));
                itemList.add(Items.createItem(Material.FLINT_AND_STEEL, "", 1));
            }
        });
    }
}
