package com.dannyandson.tinyredstone.items;

import com.dannyandson.tinyredstone.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class PanelItem extends BlockItem {

    public PanelItem()
    {
        super(Registration.REDSTONE_PANEL_BLOCK.get(),new Item.Properties());
    }

    // Note: Custom BEWLR is now registered via RegisterClientExtensionsEvent in ClientSetup

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flags)
    {
        list.add(Component.translatable("message.item.redstone_panel"));
    }
}
