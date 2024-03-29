package de.nehlen.skywars.data;

import de.nehlen.skywars.Skywars;
import lombok.Getter;

public class StringData {

    @Getter private static String prefix = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.prefix", "§2Skywars §8◆ §7");

    @Getter private static String noPerms = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.noPerms", "Keine Rechte!");

    @Getter private static String noPerm = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.noPerms", "Darauf hast du keine Rechte.");

    @Getter private static String highlightColor = Skywars.getSkywars().getGeneralConfig().getOrSetDefault("config.highlightColor", "§a");

    public static String combinate() { return prefix + noPerm; }
}
