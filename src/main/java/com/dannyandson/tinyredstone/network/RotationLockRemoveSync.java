package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.blocks.RotationLock;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RotationLockRemoveSync() implements CustomPacketPayload {

    public static final Type<RotationLockRemoveSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "rotation_lock_remove_sync"));

    public static final StreamCodec<ByteBuf, RotationLockRemoveSync> STREAM_CODEC = StreamCodec.unit(new RotationLockRemoveSync());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RotationLockRemoveSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                RotationLock.removeServerLock(serverPlayer);
            }
        });
    }
}
