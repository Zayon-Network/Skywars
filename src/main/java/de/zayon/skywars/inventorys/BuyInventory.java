package de.zayon.skywars.inventorys;

import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.CloudPermissionsPermissionManagement;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.exceptionflug.mccommons.inventories.api.Arguments;
import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.item.ItemStackWrapper;
import de.exceptionflug.mccommons.inventories.spigot.design.SpigotOnePageInventoryWrapper;
import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.StringData;
import de.zayon.zayonapi.PointsAPI.PointsAPI;
import de.zayon.zayonapi.ZayonAPI;
import de.zayon.zayonapi.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BuyInventory extends SpigotOnePageInventoryWrapper {
    private static final ConfigWrapper CONFIG_WRAPPER = ConfigFactory.create(Skywars.getSkywars().getDescription().getName() + "/inventories", BuyInventory.class, SpigotConfig.class);

    public BuyInventory(Player player, String kitName, Integer price, ItemStack icon) {
        super(player, CONFIG_WRAPPER);
        setInventoryType(de.exceptionflug.mccommons.inventories.api.InventoryType.BREWING_STAND);

        ArrayList<Object> arguments0 = new ArrayList<>(Arrays.asList(kitName, price, icon));
        set(0, Items.createItem(Material.LIME_DYE, "§aKaufen", 1), "buyKit", new Arguments(arguments0));
        set(1, Items.createItem(icon.getType(), StringData.getHighlightColor()+kitName.replace("oe", "ö").replace("ue", "ü").replace("ae", "ä")+" §7» "+price+" Punkte", 1), "nothing");
        set(2, Items.createItem(Material.RED_DYE, "§cAbbrechen", 1), "deny");
    }

    public void updateInventory() {
        super.updateInventory();
    }

    public void registerActionHandlers() {
        registerActionHandler("deny", click -> {
            ((Player)getPlayer()).openInventory((Inventory) new KitInventory(((Player)getPlayer())));
            return CallResult.DENY_GRABBING;
        });
        registerActionHandler("nothing", click -> {
            return CallResult.DENY_GRABBING;
        });
        registerActionHandler("buyKit", click -> {
            String argument = (String) click.getArguments().get(0);
            Integer argument2 = (Integer) click.getArguments().get(1);
            buyKitHandler((Player) getPlayer(), argument, argument2);
            ((Player)getPlayer()).openInventory((Inventory) new KitInventory(((Player)getPlayer())).build());
            return CallResult.DENY_GRABBING;
        });
    }

    public static void buyKitHandler(Player player, String kitName, Integer price) {
        PointsAPI pointsAPI = ZayonAPI.getZayonAPI().getPointsAPI();
        if(pointsAPI.getPoints(player) >= price) {
            pointsAPI.updatePoints(player, PointsAPI.UpdateType.REMOVE, price);
            player.sendMessage(StringData.getPrefix() + "§7Du hast erfolgreich das Kit "+ StringData.getHighlightColor() + kitName + " §7gekauft.");
            IPermissionUser iPermissionUser = Wrapper.getInstance().getUser(player.getUniqueId());
            iPermissionUser.addPermission("skywars.kits." + kitName);
            CloudPermissionsPermissionManagement.getInstance().updateUser(iPermissionUser);
            Wrapper.getInstance().updateUser(iPermissionUser);
        }else{
            player.sendMessage(StringData.getPrefix() + "§7Du hast nicht genügend Coins.");
            player.closeInventory();
        }
    }
}
