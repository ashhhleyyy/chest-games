package io.github.ashisbored.chest_games;

import io.github.ashisbored.chest_games.block.GameHeadBlock;
import io.github.ashisbored.chest_games.games.ChestGameRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class CGBlocks {
    public static final Map<String, GameHeadBlock> GAME_HEADS = Util.make(new HashMap<>(), heads -> {
        for (var entry : ChestGameRegistry.getAllGames().entrySet()) {
            heads.put(entry.getKey(), new GameHeadBlock(
                    FabricBlockSettings.copyOf(Blocks.PLAYER_HEAD),
                    entry.getValue()
            ));
        }
    });

    public static void register() {
        for (var entry : GAME_HEADS.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    private static <T extends Block> void register(String name, T block) {
        Registry.register(Registry.BLOCK, ChestGames.id(name), block);
    }
}
