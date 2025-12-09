package com.dannyandson.tinyredstone.blocks;

import com.dannyandson.tinyredstone.gui.ChopperItemHandler;
import com.dannyandson.tinyredstone.gui.ChopperMenu;
import com.dannyandson.tinyredstone.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChopperBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> items;
    private ResultContainer resultContainer = new ResultContainer();
    private ChopperMenu chopperMenu;

    private final ChopperItemHandler itemHandler = createHandler();
    private String itemType = "Tiny Block";


    public ChopperBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.CUTTER_BLOCK_ENTITY.get(), pos, state);
        this.items = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    }

    public void setCutterMenu(ChopperMenu chopperMenu) {
        this.chopperMenu = chopperMenu;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.chopperMenu != null)
            this.chopperMenu.slotsChanged(this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.tinyredstone.block_chopper");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return ChopperMenu.createChopperMenu(containerId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public ResultContainer getResultContainer() {
        return resultContainer;
    }

    /**
     * Gets the item handler for capability registration
     */
    public ChopperItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        // In 1.21+, use parseOptional with provider
        this.items.set(0, ItemStack.parseOptional(provider, compoundTag.getCompound("input_container")));
        this.resultContainer.setItem(0, ItemStack.parseOptional(provider, compoundTag.getCompound("output_container")));
        this.itemType = compoundTag.getString("output_type");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        // In 1.21+, use save with provider
        if (!this.items.get(0).isEmpty()) {
            compoundTag.put("input_container", this.items.get(0).save(provider));
        }
        if (!resultContainer.getItem(0).isEmpty()) {
            compoundTag.put("output_container", resultContainer.getItem(0).save(provider));
        }
        compoundTag.putString("output_type", itemType);
    }

    private ChopperItemHandler createHandler() {
        return new ChopperItemHandler(this);
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
        if(this.chopperMenu != null)
            this.chopperMenu.slotsChanged(null);
    }
}
