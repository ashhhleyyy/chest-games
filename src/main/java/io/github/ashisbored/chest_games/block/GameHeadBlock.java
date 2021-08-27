package io.github.ashisbored.chest_games.block;

import eu.pb4.polymer.block.VirtualHeadBlock;
import io.github.ashisbored.chest_games.games.ChestGame;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class GameHeadBlock extends Block implements VirtualHeadBlock {
    private final ChestGame game;

    public GameHeadBlock(Block.Settings settings, ChestGame game) {
        super(settings);
        this.game = game;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.ROTATION);
    }


    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand == Hand.OFF_HAND) {
            return ActionResult.FAIL;
        }

        if (world instanceof ServerWorld serverWorld && player instanceof ServerPlayerEntity spe) {
            this.game.openGame(serverWorld, spe);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return this.game.getHeadTexture();
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.ROTATION, MathHelper.floor((double)(ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15);
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return VirtualHeadBlock.super.getVirtualBlockState(state).with(Properties.ROTATION, state.get(Properties.ROTATION));
    }
}
