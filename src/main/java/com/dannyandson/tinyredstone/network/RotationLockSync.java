package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.blocks.RotationLock;
import com.dannyandson.tinyredstone.blocks.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RotationLockSync(Side rotationLock) implements CustomPacketPayload {

    public static final Type<RotationLockSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "rotation_lock_sync"));

    public static final StreamCodec<ByteBuf, RotationLockSync> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT.map(Side::fromIndex, Side::getIndex),
            RotationLockSync::rotationLock,
            RotationLockSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RotationLockSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                RotationLock.lockServerRotation(serverPlayer, packet.rotationLock());
            }
        });
    }
}
