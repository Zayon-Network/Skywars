package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
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
