package com.ming.guardhorn;

import com.ming.guardhorn.config.ModCommonConfig;
import com.ming.guardhorn.event.GuardHornEvent;
import com.ming.guardhorn.event.IllagerExtraTargetHandler;
import com.ming.guardhorn.event.ZombieHitIllagers;
import com.ming.guardhorn.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(GuardHornMod.MOD_ID)
public class GuardHornMod {
    public static final String MOD_ID = "guardhorn";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GuardHornMod(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC, "guardhorn-common.toml");

        ModItems.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(new GuardHornEvent());
        NeoForge.EVENT_BUS.register(new ZombieHitIllagers());
        NeoForge.EVENT_BUS.register(new IllagerExtraTargetHandler());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.GUARD_HORN);
        }
    }
}
