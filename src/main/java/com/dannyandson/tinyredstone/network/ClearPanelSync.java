package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClearPanelSync(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClearPanelSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "clear_panel_sync"));

    public static final StreamCodec<FriendlyByteBuf, ClearPanelSync> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClearPanelSync::pos,
            ClearPanelSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClearPanelSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                BlockEntity te = serverPlayer.level().getBlockEntity(packet.pos());
                if (te instanceof PanelTile panelTile) {
                    panelTile.removeAllCells(null);
                }
            }
        });
    }
}
