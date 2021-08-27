package io.github.ashisbored.chest_games.games;

import io.github.ashisbored.chest_games.games.minesweeper.Minesweeper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChestGameRegistry {
    private static final Map<String, ChestGame> GAMES = new HashMap<>();

    public static void register() {
        registerGame("minesweeper", new Minesweeper());
    }

    public static void registerGame(String name, ChestGame game) {
        GAMES.put(name, game);
    }

    public static Map<String, ChestGame> getAllGames() {
        // Prevent accidental mutation
        return Collections.unmodifiableMap(GAMES);
    }
}
