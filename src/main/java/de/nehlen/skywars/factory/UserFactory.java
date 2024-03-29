package de.nehlen.skywars.factory;

import de.nehlen.skywars.Skywars;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;

public class UserFactory {
    private final Skywars skywars;

    public UserFactory(Skywars skywars) {
        this.skywars = skywars;
    }

    public void createTable() {
        StringBuilder table = new StringBuilder();
        table.append("id INT(11) NOT NULL AUTO_INCREMENT, ");
        table.append("`uuid` VARCHAR(64) NOT NULL UNIQUE, ");
        table.append("`kills` INT(11) NOT NULL, ");
        table.append("`deaths` INT(11) NOT NULL, ");
        table.append("`games` INT(11) NOT NULL, ");
        table.append("`wins` INT(11) NOT NULL, ");
        table.append("PRIMARY KEY (`id`)");
        this.skywars.getDatabaseLib().executeUpdateAsync("CREATE TABLE IF NOT EXISTS skywars_stats (" + table.toString() + ")", resultSet -> {});
    }

    public CompletableFuture<Boolean> userExists(Player player) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        this.skywars.getDatabaseLib().executeQueryAsync("SELECT id FROM skywars_stats WHERE uuid = ?", resultSet -> {
            try {
                completableFuture.complete(Boolean.valueOf(resultSet.next()));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, player.getUniqueId().toString());
        return completableFuture;
    }

    public void createUser(Player player) {
        userExists(player).whenCompleteAsync((exist, throwable) -> {
            if (throwable == null && !exist.booleanValue())
                this.skywars.getDatabaseLib().executeUpdateAsync("INSERT INTO skywars_stats (uuid, kills, deaths, games, wins) VALUES (?, ?, ?, ?, ?)", resultSet -> {}, player.getUniqueId().toString(), 0, 0, 0, 0);
        });
    }

    public int getKills(Player player) {
        return (Integer) this.skywars.getDatabaseLib().get("SELECT kills FROM skywars_stats WHERE uuid = ?", player.getUniqueId().toString(), "kills");
    }

    public int getDeaths(Player player) {
        return (Integer) this.skywars.getDatabaseLib().get("SELECT deaths FROM skywars_stats WHERE uuid = ?", player.getUniqueId().toString(), "deaths");
    }

    public int getWins(Player player) {
        return (Integer) this.skywars.getDatabaseLib().get("SELECT wins FROM skywars_stats WHERE uuid = ?", player.getUniqueId().toString(), "wins");
    }

    public int getGames(Player player) {
        return (Integer) this.skywars.getDatabaseLib().get("SELECT games FROM skywars_stats WHERE uuid = ?", player.getUniqueId().toString(), "games");
    }

    public void updateKills(Player player, UpdateType updateType, int kills) {
        int newKills = 0;
        if (updateType == UpdateType.ADD) {
            newKills = getKills(player) + kills;
        } else if (updateType == UpdateType.REMOVE) {
            newKills = getKills(player) - kills;
        }
        this.skywars.getDatabaseLib().executeUpdateAsync("UPDATE skywars_stats SET kills = ? WHERE uuid = ?", resultSet -> {},newKills, player.getUniqueId().toString() );
    }

    public void updateDeaths(Player player, UpdateType updateType, int deaths) {
        int newDeaths = 0;
        if (updateType == UpdateType.ADD) {
            newDeaths = getDeaths(player) + deaths;
        } else if (updateType == UpdateType.REMOVE) {
            newDeaths = getDeaths(player) - deaths;
        }
        this.skywars.getDatabaseLib().executeUpdateAsync("UPDATE skywars_stats SET deaths = ? WHERE uuid = ?", resultSet -> {}, newDeaths, player.getUniqueId().toString());
    }

    public void updateWins(Player player, UpdateType updateType, int wins) {
        int newWins = 0;
        if (updateType == UpdateType.ADD) {
            newWins = getWins(player) + wins;
        } else if (updateType == UpdateType.REMOVE) {
            newWins = getWins(player) - wins;
        }
        this.skywars.getDatabaseLib().executeUpdateAsync("UPDATE skywars_stats SET wins = ? WHERE uuid = ?", resultSet -> {},newWins, player.getUniqueId().toString() );
    }

    public void updateGames(Player player, UpdateType updateType, int games) {
        int newGames = 0;
        if (updateType == UpdateType.ADD) {
            newGames = getGames(player) + games;
        } else if (updateType == UpdateType.REMOVE) {
            newGames = getGames(player) - games;
        }
        this.skywars.getDatabaseLib().executeUpdateAsync("UPDATE skywars_stats SET games = ? WHERE uuid = ?", resultSet -> {},newGames, player.getUniqueId().toString() );
    }

    public String getKDRatio(Player player) {
        double kd = (double) getKills(player) / (double) getDeaths(player);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(kd);
    }


    public enum UpdateType {
        ADD, REMOVE;
    }
}
