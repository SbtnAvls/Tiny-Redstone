package com.dannyandson.tinyredstone.setup;

import com.dannyandson.tinyredstone.TinyRedstone;
import com.dannyandson.tinyredstone.network.ModNetworkHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = TinyRedstone.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModSetup {

    public static void init(final FMLCommonSetupEvent event) {
        Registration.registerPanelCells();
        // Note: Network registration is now handled via RegisterPayloadHandlersEvent
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(Registration.TINY_BLOCK_OVERRIDES);
    }

}
