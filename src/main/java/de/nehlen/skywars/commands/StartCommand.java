package de.nehlen.skywars.commands;

import de.nehlen.skywars.countdowns.LobbyCountdown;
import de.nehlen.skywars.Skywars;
import de.nehlen.skywars.data.GameState;
import de.nehlen.skywars.data.StringData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final Skywars skywars;
    public StartCommand(Skywars skywars) {
        this.skywars = skywars;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {

            Player player = ((Player) commandSender);
            int startCount = 0;


            if (player.hasPermission("bingo.start") || player.hasPermission("zayon.vip+")) {
                startCount = 15;
            } else if (player.hasPermission("vip")) {
                startCount = 60;
            } else {
                player.sendMessage(StringData.getNoPerm());
                return false;
            }


            if (GameState.state == GameState.LOBBY) {
                if (Bukkit.getOnlinePlayers().size() >= 2) {
                    if (LobbyCountdown.counter > startCount) {
                        Bukkit.getScheduler().cancelTasks(this.skywars);
                        LobbyCountdown.startLobbyCountdown(false);
                        LobbyCountdown.counter = startCount;
                    } else {
                        player.sendMessage(StringData.getPrefix() + "§7Das Spiel ist bereits gestartet!");
                    }

                } else {
                    player.sendMessage(StringData.getPrefix() + "§7Es sind nicht genügen Spieler Online!");
                }
            } else {
                player.sendMessage(StringData.getPrefix() + "§7Das Spiel hat bereits gestartet!");
            }

        } else {
            commandSender.sendMessage("Dieser Befehl kann nur Ingame ausgeführt werden.");
        }

        return false;
    }
}
