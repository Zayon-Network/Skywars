package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.countdowns.IngameCountdown;
import de.nehlen.skywars.data.GameData;
import de.nehlen.skywars.data.GameState;
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
            if(GameState.state == GameState.LOBBY || (GameState.state == GameState.INGAME && IngameCountdown.getCounter() < 30) || GameState.state == GameState.END) {
                e.setCancelled(true);
            }
            if(!GameData.getIngame().contains(p)) {
                e.setCancelled(true);
            }
        }
    }
}
