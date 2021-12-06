package de.nehlen.skywars.inventorys;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.exceptionflug.mccommons.inventories.api.Arguments;
import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.spigot.design.SpigotOnePageInventoryWrapper;
import de.nehlen.gameapi.Gameapi;
import de.nehlen.gameapi.ItemsAPI.Items;
import de.nehlen.gameapi.PointsAPI.PointsAPI;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.StringData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BuyInventory extends SpigotOnePageInventoryWrapper {
    private static final ConfigWrapper CONFIG_WRAPPER = ConfigFactory.create(Skywars.getSkywars().getDescription().getName() + "/inventories", BuyInventory.class, SpigotConfig.class);

    public BuyInventory(Player player, String kitName, Integer price, ItemStack icon) {
        super(player, CONFIG_WRAPPER);
        setInventoryType(de.exceptionflug.mccommons.inventories.api.InventoryType.BREWING_STAND);

        ArrayList<Object> arguments0 = new ArrayList<>(Arrays.asList(kitName, price, icon));
        set(0, Items.createItem(Material.LIME_DYE, "§aKaufen", 1), "buyKit", new Arguments(arguments0));
        set(1, Items.createItem(icon.getType(), StringData.getHighlightColor()+kitName.replace("oe", "ö").replace("ue", "ü").replace("ae", "ä")+" §7» "+price+" Punkte", 1), "nothing");
        set(2, Items.createItem(Material.RED_DYE, "§cAbbrechen", 1), "deny");
        if(Gameapi.getGameapi().getPointsAPI().getPoints(player) < price) {
            set(4, Items.createItem(Material.RED_DYE, "§cDu hast leider nicht genug Punkte.", 1), "nothing");
        } else {
            set(4, Items.createItem(Material.LIME_DYE, "§aDu hast genug Punkte.", 1), "nothing");
        }
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
        PointsAPI pointsAPI = Gameapi.getGameapi().getPointsAPI();
        if(pointsAPI.getPoints(player) >= price) {

            pointsAPI.updatePoints(player, PointsAPI.UpdateType.REMOVE, price);
            player.sendMessage(StringData.getPrefix() + "§7Du hast erfolgreich das Kit "+ StringData.getHighlightColor() + kitName + " §7gekauft.");
            IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
            iPermissionUser.addPermission("skywars.kits." + kitName);
            CloudNetDriver.getInstance().getPermissionManagement().updateUser(iPermissionUser);
        }else{
            player.sendMessage(StringData.getPrefix() + "§7Du hast nicht genügend Coins.");
            player.closeInventory();
        }
    }
}
