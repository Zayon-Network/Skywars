package de.zayon.skywars.listener;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.countdowns.IngameCountdown;
import de.zayon.skywars.data.GameData;
import de.zayon.skywars.data.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final Skywars skywars;
    public DamageListener(Skywars skywars) {this.skywars = skywars;}

    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(GameState.state == GameState.LOBBY || (GameState.state != GameState.INGAME && IngameCountdown.counter < (GameData.getMaxGameTime()-600))) {
                e.setCancelled(true);
            }
            if(!GameData.getIngame().contains(p)) {
                e.setCancelled(true);
            }
        }
    }
}
