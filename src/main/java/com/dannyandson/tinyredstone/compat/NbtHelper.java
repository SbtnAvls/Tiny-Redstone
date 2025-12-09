package com.dannyandson.tinyredstone.compat;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;

/**
 * Helper class for handling ItemStack NBT operations in 1.21+
 * The NBT API was replaced with Data Components system.
 */
public class NbtHelper {

    /**
     * Check if an ItemStack has custom NBT data
     */
    public static boolean hasTag(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && !customData.isEmpty();
    }

    /**
     * Get the custom NBT data from an ItemStack
     * @return the CompoundTag or null if none exists
     */
    @Nullable
    public static CompoundTag getTag(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && !customData.isEmpty()) {
            return customData.copyTag();
        }
        return null;
    }

    /**
     * Get the custom NBT data from an ItemStack, creating empty if needed
     * @return the CompoundTag, never null
     */
    public static CompoundTag getOrCreateTag(ItemStack stack) {
        CompoundTag tag = getTag(stack);
        if (tag == null) {
            tag = new CompoundTag();
            setTag(stack, tag);
        }
        return tag;
    }

    /**
     * Set the custom NBT data on an ItemStack
     */
    public static void setTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /**
     * Get a specific element from the custom NBT data
     * @return the CompoundTag element or null if not found
     */
    @Nullable
    public static CompoundTag getTagElement(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        if (tag != null && tag.contains(key)) {
            return tag.getCompound(key);
        }
        return null;
    }

    /**
     * Check if the custom NBT contains a specific key
     */
    public static boolean containsTag(ItemStack stack, String key) {
        CompoundTag tag = getTag(stack);
        return tag != null && tag.contains(key);
    }
}
