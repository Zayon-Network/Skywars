package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.manager.KitManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class PlayerFishListener implements Listener {
    @EventHandler
    public void handleFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if ( Skywars.getSkywars().getKitManager().getCurrentKit(player).equals(KitManager.Kit.ENTERHACKEN) && (
                event.getState().equals(PlayerFishEvent.State.IN_GROUND) || event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))) {
            Location location = player.getLocation();
            Location to = event.getHook().getLocation();
            location.setY(location.getY() + 0.5D);
            player.teleport(location);
            double g = -0.08D;
            double d = to.distance(location);
            double t = d;
            double v_x = (1.0D + 0.07D * t) * (to.getX() - location.getX()) / t;
            double v_y = (1.0D + 0.03D * t) * (to.getY() - location.getY()) / t - 0.5D * g * t;
            double v_z = (1.0D + 0.07D * t) * (to.getZ() - location.getZ()) / t;
            Vector v = player.getVelocity();
            v.setX(v_x);
            v.setY(v_y);
            v.setZ(v_z);
            player.setVelocity(v);
        }
    }
}
