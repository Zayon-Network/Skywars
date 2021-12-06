package de.nehlen.skywars.data;

public enum GameState {

    LOBBY, INGAME, END;

    public static GameState state = LOBBY;

    private GameState() {}
}
