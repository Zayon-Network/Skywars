package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ProtectionListener implements Listener {

    private final Skywars skywars;
    public ProtectionListener(Skywars skywars) {this.skywars = skywars;}

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(GameState.state == GameState.LOBBY) {
            event.setCancelled(true);
        } else if(player.getGameMode() == GameMode.SPECTATOR){
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(GameState.state == GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChest(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.CHEST) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onChest(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.CHEST) {
            event.setCancelled(true);
        }

    }


}
