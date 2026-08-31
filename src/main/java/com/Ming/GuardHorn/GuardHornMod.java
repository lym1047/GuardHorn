package com.Ming.GuardHorn;

import com.Ming.GuardHorn.event.GuardHornEvent;
import com.Ming.GuardHorn.event.IllagerExtraTargetHandler;
import com.Ming.GuardHorn.event.ZombieHitIllagers;
import com.Ming.GuardHorn.item.ModItems;
import com.Ming.GuardHorn.config.ModCommonConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import tallestegg.guardvillagers.GuardVillagers;

@Mod(GuardHornMod.MOD_ID)
public class GuardHornMod {
    public static final String MOD_ID = "guardhorn";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GuardHornMod() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC, "guardhorn-common.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(new GuardHornEvent());
        MinecraftForge.EVENT_BUS.register(new ZombieHitIllagers());
        MinecraftForge.EVENT_BUS.register(new IllagerExtraTargetHandler());

        ModItems.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.GUARD_HORN);
        }
    }
}
