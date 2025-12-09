package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.api.IPanelCell;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PanelCellSync(BlockPos pos, int cellIndex, CompoundTag nbt) implements CustomPacketPayload {

    public static final Type<PanelCellSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "panel_cell_sync"));

    public static final StreamCodec<FriendlyByteBuf, PanelCellSync> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PanelCellSync::pos,
            ByteBufCodecs.VAR_INT, PanelCellSync::cellIndex,
            ByteBufCodecs.COMPOUND_TAG, PanelCellSync::nbt,
            PanelCellSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PanelCellSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BlockEntity te = Minecraft.getInstance().level.getBlockEntity(packet.pos());
            if (te instanceof PanelTile panelTile) {
                PanelCellPos cellPos = PanelCellPos.fromIndex(panelTile, packet.cellIndex());
                IPanelCell cell = cellPos.getIPanelCell();
                if (cell != null) {
                    cell.readNBT(packet.nbt());
                }
            }
        });
    }
}
