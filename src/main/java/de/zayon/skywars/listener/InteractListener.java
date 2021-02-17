package de.zayon.skywars.listener;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import de.zayon.skywars.data.StringData;
import de.zayon.skywars.inventorys.KitInventory;
import de.zayon.skywars.inventorys.TeamSelectInventroy;
import de.zayon.skywars.sidebar.util.UtilFunctions;
import de.zayon.zayonapi.TeamAPI.Team;
import de.zayon.zayonapi.ZayonAPI;
import de.zayon.zayonapi.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteractListener implements Listener {

    private ArrayList<Chest> chestCache = new ArrayList<>();
    private final Skywars skywars;

    public InteractListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();


        if (GameState.state == GameState.LOBBY) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Teamauswahl")) {
                player.openInventory(new TeamSelectInventroy(player).build());
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Zurück zur Lobby")) {
                BridgePlayerManager.getInstance().proxySendPlayer(BridgePlayerManager.getInstance().getOnlinePlayer(player.getUniqueId()), "Lobby-1");
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Starte das Spiel")) {
                if (Bukkit.getOnlinePlayers().size() >= 2) {
                    Bukkit.getScheduler().cancelTasks(Skywars.getSkywars());
                    Skywars.getSkywars().getLobbyCountdown().startLobbyCountdown(true);
                } else {
                    player.sendMessage(StringData.getPrefix() + "Es sind leider " + StringData.getHighlightColor() + "nicht genug Spieler §7Im Spiel.");
                }
            } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Kitauswahl")) {
                player.openInventory(new KitInventory(player).build());
            }
        } else if (GameState.state == GameState.INGAME) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.PHYSICAL)) {
                if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {

                    Location loc = event.getClickedBlock().getLocation();
                    Chest chest = (Chest) loc.getBlock().getState();

                    if (!chestCache.contains(chest)) {

                        Inventory inventory = chest.getBlockInventory();
                        List<ItemStack> items = GameData.getItemList();
                        int l = UtilFunctions.getRandomNumberInRange(10, 20);
                        while (l != 0) {
                            l--;
                            Random r2 = new Random();
                            Random r3 = new Random();
                            int n2 = r2.nextInt(27);
                            int n3 = r3.nextInt(items.size());
                            inventory.setItem(n2, items.get(n3));

                        }
                        chestCache.add(chest);
                    }
                    return;

                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                        for (Entity ent : player.getNearbyEntities(500D, 25D, 500D)) {
                            if (ent instanceof Player) {
                                Player near = (Player) ent;
                                player.setCompassTarget(near.getLocation());
                                if ((GameData.getIngame().contains(near))) {
                                    player.sendTitle("", "§9" + near.getName() + " §7» §9" + ((int) player.getLocation().distance(near.getLocation())) + "§7 Blöcke", 40, 120, 60);
                                }
                            }
                        }
                    }
                }
            }
        }




    }
}
