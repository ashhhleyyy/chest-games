package io.github.ashisbored.chest_games;

import eu.pb4.polymer.item.VirtualHeadBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class CGItems {
    public static final Map<String, VirtualHeadBlockItem> GAME_HEADS = Util.make(new HashMap<>(), heads -> {
        for (var entry : CGBlocks.GAME_HEADS.entrySet()) {
            heads.put(entry.getKey(), new VirtualHeadBlockItem(
                    entry.getValue(),
                    new Item.Settings()
            ));
        }
    });

    public static void register() {
        for (var entry : GAME_HEADS.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    private static <T extends Item> void register(String name, T block) {
        Registry.register(Registry.ITEM, ChestGames.id(name), block);
    }
}
