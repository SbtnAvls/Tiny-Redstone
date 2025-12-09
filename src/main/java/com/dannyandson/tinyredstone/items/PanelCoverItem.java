package com.dannyandson.tinyredstone.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class PanelCoverItem extends PanelCellItem{

    @Override
    public InteractionResult useOn(UseOnContext context) {
       return InteractionResult.PASS;
    }

    // Note: Custom BEWLR is now registered via RegisterClientExtensionsEvent in ClientSetup

    @Override
    public Component getName(ItemStack stack) {
        // In 1.21+, item components replace NBT tags
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            String thisName = super.getName(stack).getString();
            String fromBlockName = null;
            CompoundTag itemNBT = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            CompoundTag madeFromTag = itemNBT.getCompound("made_from");
            if (madeFromTag.contains("namespace")) {
                fromBlockName = (Component.translatable("block." + madeFromTag.getString("namespace") + "." + madeFromTag.getString("path"))).getString();
            }
            return Component.nullToEmpty(thisName + " (" + fromBlockName + ")");
        }

        return super.getName(stack);
    }
}
