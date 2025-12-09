package com.dannyandson.tinyredstone.network;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.blocks.ChopperBlockEntity;
import com.dannyandson.tinyredstone.blocks.RenderHelper;
import com.dannyandson.tinyredstone.codec.TinyBlockData;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ValidTinyBlockCacheSync(ResourceLocation itemRegistryName, Optional<BlockPos> chopperPos) implements CustomPacketPayload {

    public static final Type<ValidTinyBlockCacheSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TinyRedstone.MODID, "valid_tiny_block_cache_sync"));

    public static final StreamCodec<FriendlyByteBuf, ValidTinyBlockCacheSync> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ValidTinyBlockCacheSync decode(FriendlyByteBuf buf) {
            ResourceLocation registryName = buf.readResourceLocation();
            Optional<BlockPos> chopperPos = Optional.empty();
            if (buf.readBoolean()) {
                chopperPos = Optional.of(buf.readBlockPos());
            }
            return new ValidTinyBlockCacheSync(registryName, chopperPos);
        }

        @Override
        public void encode(FriendlyByteBuf buf, ValidTinyBlockCacheSync packet) {
            buf.writeResourceLocation(packet.itemRegistryName());
            buf.writeBoolean(packet.chopperPos().isPresent());
            packet.chopperPos().ifPresent(buf::writeBlockPos);
        }
    };

    // Constructor helper for when chopperPos is nullable
    public ValidTinyBlockCacheSync(@Nullable BlockPos chopperPos, ResourceLocation itemRegistryName) {
        this(itemRegistryName, Optional.ofNullable(chopperPos));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(ValidTinyBlockCacheSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ResourceLocation texture1 = ResourceLocation.fromNamespaceAndPath(packet.itemRegistryName().getNamespace(), "block/" + packet.itemRegistryName().getPath());
            ResourceLocation texture2 = ResourceLocation.fromNamespaceAndPath(packet.itemRegistryName().getNamespace(), "block/" + packet.itemRegistryName().getPath() + "_side");
            TextureAtlasSprite sprite1 = RenderHelper.getSprite(texture1);
            TextureAtlasSprite sprite2 = RenderHelper.getSprite(texture2);
            if (sprite1 != RenderHelper.getSprite(TextureManager.INTENTIONAL_MISSING_TEXTURE) || sprite2 != RenderHelper.getSprite(TextureManager.INTENTIONAL_MISSING_TEXTURE)) {
                // Tell server chopper menu at block pos to update
                ModNetworkHandler.sendToServer(new ValidTinyBlockCacheSync(packet.chopperPos().orElse(null), packet.itemRegistryName()));
            }
        });
    }

    public static void handleServer(ValidTinyBlockCacheSync packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!TinyBlockData.validBlockTextureCache.contains(packet.itemRegistryName().toString())) {
                TinyBlockData.validBlockTextureCache.add(packet.itemRegistryName().toString());
            }
            if (packet.chopperPos().isPresent() && ctx.player() instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.level().getBlockEntity(packet.chopperPos().get()) instanceof ChopperBlockEntity chopperBlockEntity) {
                    chopperBlockEntity.setChanged();
                }
            }
        });
    }
}
