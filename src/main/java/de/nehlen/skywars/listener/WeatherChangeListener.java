package de.nehlen.skywars.listener;

import de.nehlen.skywars.Skywars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    private final Skywars skywars;
    public WeatherChangeListener(Skywars skywars) {
        this.skywars = skywars;
    }

    @EventHandler
    public void handleWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);

    }
}
