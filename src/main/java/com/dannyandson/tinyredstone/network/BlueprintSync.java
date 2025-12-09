package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.items.Blueprint;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BlueprintSync(CompoundTag nbt) implements CustomPacketPayload {

    public static final Type<BlueprintSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "blueprint_sync"));

    public static final StreamCodec<FriendlyByteBuf, BlueprintSync> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, BlueprintSync::nbt,
            BlueprintSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BlueprintSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                ItemStack blueprint = serverPlayer.getMainHandItem();
                if (blueprint.getItem() instanceof Blueprint) {
                    // In 1.21+, use data components instead of setTag
                    blueprint.set(DataComponents.CUSTOM_DATA, CustomData.of(packet.nbt()));
                }
            }
        });
    }
}
