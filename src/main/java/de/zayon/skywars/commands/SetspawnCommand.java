package de.zayon.skywars.commands;

import de.zayon.skywars.Skywars;
import de.zayon.skywars.data.StringData;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetspawnCommand implements CommandExecutor {
    private final Skywars skywars;

    public SetspawnCommand(Skywars skywars) {
        this.skywars = skywars;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player)commandSender;
        if (!player.hasPermission("rang.admin")) {
            player.sendMessage(StringData.combinate());
            return true;
        }
        if (args.length == 1 || args.equals("spawn")) {
            Location location = player.getLocation();
            this.skywars.getLocationConfig().getOrSetDefault("config.location.spawn", location);
            player.sendMessage(StringData.getPrefix() + "§7 Spawn gesetzt§8.");
        } else if (args.length == 1 || args.equals("teams")) {
            int index = 0;
            Location location = player.getLocation();
            this.skywars.getLocationConfig().getOrSetDefault("config.location.team." +index, location);
            player.sendMessage(StringData.getPrefix() + "§7 Spawn gesetzt§8.");
            index++;
        }
        return false;
    }
}
