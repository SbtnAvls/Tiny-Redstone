package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlaySound(BlockPos pos, String namespace, String path, float volume, float pitch) implements CustomPacketPayload {

    public static final Type<PlaySound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "play_sound"));

    public static final StreamCodec<FriendlyByteBuf, PlaySound> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PlaySound::pos,
            ByteBufCodecs.STRING_UTF8, PlaySound::namespace,
            ByteBufCodecs.STRING_UTF8, PlaySound::path,
            ByteBufCodecs.FLOAT, PlaySound::volume,
            ByteBufCodecs.FLOAT, PlaySound::pitch,
            PlaySound::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlaySound packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BlockEntity te = Minecraft.getInstance().level.getBlockEntity(packet.pos());
            if (te instanceof PanelTile) {
                te.getLevel().playLocalSound(
                        packet.pos().getX(), packet.pos().getY(), packet.pos().getZ(),
                        SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(packet.namespace(), packet.path())),
                        SoundSource.BLOCKS, packet.volume(), packet.pitch(), false
                );
            }
        });
    }
}
