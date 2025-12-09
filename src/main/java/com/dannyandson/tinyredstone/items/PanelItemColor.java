package com.dannyandson.tinyredstone.items;

import com.dannyandson.tinyredstone.compat.NbtHelper;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class PanelItemColor implements ItemColor {
    @Override
    public int getColor(ItemStack p_getColor_1_, int p_getColor_2_)
    {
        CompoundTag stackTag = NbtHelper.getTag(p_getColor_1_);
        if (stackTag != null && stackTag.contains("BlockEntityTag") ) {
            CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("color")) {
                int color = blockEntityTag.getInt("color");
                return color;
            }
        }
        return DyeColor.GRAY.getTextColor();
    }
}
