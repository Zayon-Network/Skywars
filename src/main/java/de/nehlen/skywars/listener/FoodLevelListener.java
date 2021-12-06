package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelListener implements Listener {

    private final Skywars skywars;
    public FoodLevelListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleLevelChange(FoodLevelChangeEvent event) {
        if(!GameData.getIngame().contains(event.getEntity()))
            event.setCancelled(true);
    }
}
