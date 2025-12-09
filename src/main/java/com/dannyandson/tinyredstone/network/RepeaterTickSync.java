package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.api.IPanelCell;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import com.dannyandson.tinyredstone.blocks.panelcells.Repeater;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RepeaterTickSync(BlockPos pos, int cellIndex, int ticks) implements CustomPacketPayload {

    public static final Type<RepeaterTickSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "repeater_tick_sync"));

    public static final StreamCodec<FriendlyByteBuf, RepeaterTickSync> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RepeaterTickSync::pos,
            ByteBufCodecs.VAR_INT, RepeaterTickSync::cellIndex,
            ByteBufCodecs.VAR_INT, RepeaterTickSync::ticks,
            RepeaterTickSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RepeaterTickSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                BlockEntity te = serverPlayer.level().getBlockEntity(packet.pos());
                if (te instanceof PanelTile panelTile) {
                    PanelCellPos cellPos = PanelCellPos.fromIndex(panelTile, packet.cellIndex());
                    IPanelCell cell = cellPos.getIPanelCell();
                    if (cell instanceof Repeater repeater) {
                        repeater.setTicks(packet.ticks());
                        panelTile.flagSync();
                    }
                }
            }
        });
    }
}
