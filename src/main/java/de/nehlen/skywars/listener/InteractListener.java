package de.nehlen.skywars.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameData;
import de.nehlen.skywars.data.GameState;
import de.nehlen.skywars.data.StringData;
import de.nehlen.skywars.inventorys.KitInventory;
import de.nehlen.skywars.inventorys.TeamSelectInventroy;
import de.nehlen.skywars.sidebar.util.UtilFunctions;
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

    private final Skywars skywars;
    private ArrayList<Chest> chestCache = new ArrayList<>();
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

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
                playerManager.getPlayerExecutor(playerManager.getOnlinePlayer(player.getUniqueId())).connectToGroup("Lobby", ServerSelectorType.RANDOM);
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
