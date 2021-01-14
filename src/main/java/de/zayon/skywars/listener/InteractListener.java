package de.zayon.skywars.listener;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.StringData;
import de.zayon.skywars.inventorys.KitInventory;
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

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {

                Location loc = event.getClickedBlock().getLocation();
                Chest chest = (Chest) loc.getBlock();

                if (!chestCache.contains(chest)) {

                    Inventory inventory = chest.getBlockInventory();
                    List<ItemStack> items = GameData.getItemList();
                    Random r = new Random();
                    int l = r.nextInt(15);
                    if (l < 10) {
                        l=+8;
                    }

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

            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Player player = event.getPlayer();
            try {
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
                } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Teamauswahl")) {
                    Inventory inv = Bukkit.createInventory(null, (int) Math.ceil(GameData.getTeamAmount() / 9) * 9, StringData.getHighlightColor() + "Team auswahl");

                    int i = 0;
                    for (Team t : ZayonAPI.getZayonAPI().getTeamAPI().getRegisteredTeams()) {
                        if (t.getRegisteredPlayers().contains(player)) {
                            inv.setItem(i, Items.createLore(Material.LIME_DYE, StringData.getHighlightColor() + "Team-" + (i + 1), StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize(), 1));
                        } else if (t.size() == t.getMaxTeamSize()) {
                            inv.setItem(i, Items.createLore(Material.RED_DYE, StringData.getHighlightColor() + "Team-" + (i + 1), StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize(), 1));
                        } else {
                            inv.setItem(i, Items.createLore(Material.LIGHT_GRAY_DYE, StringData.getHighlightColor() + "Team-" + (i + 1), StringData.getHighlightColor() + t.size() + "§7/" + StringData.getHighlightColor() + t.getMaxTeamSize(), 1));
                        }
                        i++;
                    }

                    player.openInventory(inv);
                } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Zurück zur Lobby")) {
                    BridgePlayerManager.getInstance().proxySendPlayer(BridgePlayerManager.getInstance().getOnlinePlayer(player.getUniqueId()), "Lobby-1");
                } else if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Starte das Spiel")) {
                    if (Bukkit.getOnlinePlayers().size() >= 2) {
                        Bukkit.getScheduler().cancelTasks(Skywars.getSkywars());
                        Skywars.getSkywars().getLobbyCountdown().startLobbyCountdown(true);
                    } else {
                        player.sendMessage(StringData.getPrefix() + "Es sind leider " + StringData.getHighlightColor() + "nicht genug Spieler §7Im Spiel.");
                    }
                } else if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§7Kitauswahl")) {
                    player.openInventory((Inventory) new KitInventory(player).build());
                }
            } catch (Exception ex) {
            }
        }

    }
}
