package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {

    private final Skywars skywars;

    public BuildListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleBuild(BlockPlaceEvent event) {

        if(GameState.state != GameState.INGAME) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void handleBuild(BlockBreakEvent event) {

        if(GameState.state != GameState.INGAME) {
            event.setCancelled(true);
        }

    }
}
