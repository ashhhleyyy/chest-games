package io.github.ashisbored.chest_games.games;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public abstract class ChestGame {
    private final String headTexture;

    protected ChestGame(String headTexture) {
        this.headTexture = headTexture;
    }

    public String getHeadTexture() {
        return this.headTexture;
    }

    public abstract void openGame(ServerWorld world, ServerPlayerEntity player);
}
