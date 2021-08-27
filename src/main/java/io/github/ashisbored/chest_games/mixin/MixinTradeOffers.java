package io.github.ashisbored.chest_games.mixin;

import eu.pb4.polymer.item.VirtualHeadBlockItem;
import io.github.ashisbored.chest_games.CGItems;
import io.github.ashisbored.chest_games.ChestGames;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TradeOffers.class)
public class MixinTradeOffers {
    @Shadow @Final public static Int2ObjectMap<TradeOffers.Factory[]> WANDERING_TRADER_TRADES;

    @SuppressWarnings("UnresolvedMixinReference") // MCDev doesn't understand static initialisers
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void chestGames_appendCustomTradeOffers(CallbackInfo ci) {
        // mojang, why do you use arrays for this -_-
        TradeOffers.Factory[] factories = WANDERING_TRADER_TRADES.get(1);
        TradeOffers.Factory[] newFactories = new TradeOffers.Factory[factories.length + CGItems.GAME_HEADS.size()];
        System.arraycopy(factories, 0, newFactories, 0, factories.length);
        int i = 0;
        for (VirtualHeadBlockItem item : CGItems.GAME_HEADS.values()) {
            newFactories[factories.length + i] = (entity, random) ->
                    // I pulled these numbers from vanilla
                    new TradeOffer(new ItemStack(Items.EMERALD, 6), new ItemStack(item, 1),
                            1, 1, 0.05f);

            i++;
        }
        WANDERING_TRADER_TRADES.put(1, newFactories);
        ChestGames.LOGGER.info("Trader offers injected!");
    }
}
