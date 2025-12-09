package com.dannyandson.tinyredstone.items;

import com.dannyandson.tinyredstone.blocks.PanelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import java.util.List;

public class RedstoneWrench extends Item {
    public RedstoneWrench() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            Player player = context.getPlayer();
            BlockPos pos = context.getClickedPos();

            if (player != null && player.isCrouching()) {
                // Make sure the block get activated if it is a BaseBlockNew
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof PanelBlock panelBlock) {
                    BlockHitResult hitResult = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside());
                    return panelBlock.useWithoutItem(state, world, pos, player, hitResult);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flags)
    {
        list.add(Component.translatable("message.item.redstone_wrench"));
    }

    // Note: canPlayerBreakBlockWhileHolding was removed in 1.21

    public BlockHitResult getHitResult(Level world, Player player)
    {
        return Item.getPlayerPOVHitResult(world,player, ClipContext.Fluid.ANY);
    }
}
