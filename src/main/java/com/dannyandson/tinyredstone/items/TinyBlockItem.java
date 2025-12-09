package com.dannyandson.tinyredstone.items;

import com.dannyandson.tinyredstone.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class TinyBlockItem extends PanelCellItem {

    @Override
    public Component getName(ItemStack stack) {
        String thisName = super.getName(stack).getString();
        String fromBlockName = null;
        // In 1.21+, item components replace NBT tags
        // Check for custom data component
        if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
            net.minecraft.nbt.CompoundTag itemNBT = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag();
            net.minecraft.nbt.CompoundTag madeFromTag = itemNBT.getCompound("made_from");
            if (madeFromTag.contains("namespace")) {
                fromBlockName = (Component.translatable("block." + madeFromTag.getString("namespace") + "." + madeFromTag.getString("path"))).getString();
            }
        }
        if (fromBlockName==null){
            if (stack.getItem()== Registration.TINY_SOLID_BLOCK.get()){
                    fromBlockName = Component.translatable("block.minecraft.white_wool").getString();
            } else if (stack.getItem()== Registration.TINY_TRANSPARENT_BLOCK.get()){
                fromBlockName = Component.translatable("block.minecraft.glass").getString();
            }
        }
        return Component.nullToEmpty(thisName + " (" + fromBlockName + ")");
    }

    // Note: Custom BEWLR is now registered via RegisterClientExtensionsEvent in ClientSetup
}
