package com.dannyandson.tinyredstone.datagen;

import com.dannyandson.tinyredstone.TinyRedstone;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TinyRedstone.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            //generator.addProvider(new Recipes(generator));
            //generator.addProvider(new LootTables(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new BlockStates(generator.getPackOutput(), event.getExistingFileHelper()));
            generator.addProvider(true, new Items(generator, event.getExistingFileHelper()));
        }

    }
}
