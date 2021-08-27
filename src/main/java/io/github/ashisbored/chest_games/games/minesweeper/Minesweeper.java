package io.github.ashisbored.chest_games.games.minesweeper;

import io.github.ashisbored.chest_games.games.ChestGame;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class Minesweeper extends ChestGame {
    private static final String HEAD_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkN2ZjOGUzYTk1OWFkZTdkOWNmNjYzZjFlODJkYjc5NzU1NDNlMjg4YWI4ZDExZWIyNTQxODg4MjEzNTI2In19fQ==";

    public Minesweeper() {
        super(HEAD_TEXTURE);
    }

    @Override
    public void openGame(ServerWorld world, ServerPlayerEntity player) {
        new MinesweeperGui(player).open();
    }
}
