package com.Ming.GuardHorn.item;

import com.Ming.GuardHorn.GuardHornMod;
import com.Ming.GuardHorn.features.GuardHornItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GuardHornMod.MOD_ID);

    public static final RegistryObject<Item> GUARD_HORN =
            ITEMS.register("guard_horn", () -> new GuardHornItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
