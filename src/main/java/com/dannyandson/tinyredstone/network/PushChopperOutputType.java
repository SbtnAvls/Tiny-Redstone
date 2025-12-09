package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.blocks.ChopperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PushChopperOutputType(String itemType, BlockPos pos) implements CustomPacketPayload {

    public static final Type<PushChopperOutputType> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "push_chopper_output_type"));

    public static final StreamCodec<FriendlyByteBuf, PushChopperOutputType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PushChopperOutputType::itemType,
            BlockPos.STREAM_CODEC, PushChopperOutputType::pos,
            PushChopperOutputType::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PushChopperOutputType packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                BlockEntity te = serverPlayer.level().getBlockEntity(packet.pos());
                if (te instanceof ChopperBlockEntity chopper) {
                    chopper.setItemType(packet.itemType());
                }
            }
        });
    }
}
