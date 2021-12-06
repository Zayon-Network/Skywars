package de.nehlen.skywars.inventorys;

import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.exceptionflug.mccommons.core.Converters;
import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.spigot.design.SpigotOnePageInventoryWrapper;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.StringData;
import de.nehlen.skywars.manager.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitInventory extends SpigotOnePageInventoryWrapper {
    private static final ConfigWrapper CONFIG_WRAPPER = ConfigFactory.create(Skywars.getSkywars().getDescription().getName() + "/inventories", KitInventory.class, SpigotConfig.class);

    public KitInventory(Player player) {
        super(player, CONFIG_WRAPPER);
        setTitle("§2Kitauswahl §7» " + Gameapi.getGameapi().getPointsAPI().getPoints(player) + " Points");
        getInventoryItemMap().forEach((key, item) -> {
            if(getPlayer().hasPermission("skywars.kits." + item.getArguments().get(0).toString().toLowerCase()) || getPlayer().hasPermission("skywars.kits.*")) {
                List<String> lore = item.getItemStackWrapper().getLore();
                lore.add("");
                lore.add("§aGEKAUFT!");
                item.getItemStackWrapper().setLore(lore);
            }
        });
        Bukkit.getConsoleSender().sendMessage("jajaja");
        updateInventory();
    }

    public void updateInventory() {
        super.updateInventory();
    }

    public void registerActionHandlers() {
        registerActionHandler("selectKit", click -> {
            String argument = (String) click.getArguments().get(0);
            Integer argument2 = (Integer) click.getArguments().get(1);
            selectKitHandler((Player) getPlayer(), argument, argument2, Converters.convert(click.getClickedItem(), ItemStack.class));
            return CallResult.DENY_GRABBING;
        });
    }

    public static void selectKitHandler(Player player, String kitName, Integer price, ItemStack icon) {

        if((player.hasPermission("skywars.kits." + kitName.toLowerCase())) || (player.hasPermission("skywars.kits.*"))) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.sendMessage(StringData.getPrefix() + "§7Du hast das Kit "+StringData.getHighlightColor()+kitName.replaceAll("oe", "ö").replaceAll("ue", "ü").replaceAll("ae", "ä")+" §7ausgewählt.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 3.0F);
            player.closeInventory();

            updateKitCache(player, kitName);
        } else {
            player.openInventory((Inventory) new BuyInventory(player, kitName, price, icon).build());
        }
    }

    private static void updateKitCache(Player player, String kitName) {
        if (Skywars.getSkywars().getKitManager().getKitCache().containsKey(player))
            Skywars.getSkywars().getKitManager().removeKit(player);
        Skywars.getSkywars().getKitManager().setKit(player, KitManager.Kit.valueOf(kitName));
    }
}
