package de.zayon.skywars.inventorys;

import de.exceptionflug.mccommons.config.shared.ConfigFactory;
import de.exceptionflug.mccommons.config.shared.ConfigWrapper;
import de.exceptionflug.mccommons.config.spigot.SpigotConfig;
import de.exceptionflug.mccommons.core.Converters;
import de.exceptionflug.mccommons.inventories.api.Arguments;
import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.InventoryType;
import de.exceptionflug.mccommons.inventories.spigot.design.SpigotOnePageInventoryWrapper;
import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.StringData;
import de.zayon.skywars.manager.KitManager;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.ZayonAPI;
import de.zayon.zayonapi.items.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class TeamSelectInventroy extends SpigotOnePageInventoryWrapper {

    private static final ConfigWrapper CONFIG_WRAPPER = ConfigFactory.create(Skywars.getSkywars().getDescription().getName() + "/inventories", KitInventory.class, SpigotConfig.class);

    public TeamSelectInventroy(Player player) {
        super(player, CONFIG_WRAPPER);
        setInventoryType(InventoryType.getChestInventoryWithRows((int) Math.ceil(GameData.getTeamAmount() / 9)));
        setTitle("§7Teamauswahl");

        for (Team t : ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams()) {
            ArrayList<Object> argument = new ArrayList<>();

            if (t.getRegisteredPlayers().contains(player)) {
                argument.add(t);
                add(Items.createLore(Material.LIME_DYE, t.getTeamName(), StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize(), 1), "nothing");
            } else if (t.size() == t.getMaxTeamSize()) {
                add(Items.createLore(Material.RED_DYE, t.getTeamName(), StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize(), 1), "nothing");
            } else {
                add(Items.createLore(Material.LIGHT_GRAY_DYE, t.getTeamName(), StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize(), 1), "selectTeam", new Arguments(argument));
            }
        }
    }

    public void updateInventory() {
        super.updateInventory();
    }

    public void registerActionHandlers() {
        registerActionHandler("nothing", click -> {
            return CallResult.DENY_GRABBING;
        });
        registerActionHandler("selectTeam", click -> {
            Team argument = (Team) click.getArguments().get(0);
            argument.addPlayer((Player) getPlayer());
            return CallResult.DENY_GRABBING;
        });
    }
}
