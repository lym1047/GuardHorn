package com.Ming.GuardHorn.config;

import com.Ming.GuardHorn.GuardHornMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GuardHornMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue COOLDOWN_TICKS = BUILDER
            .comment("号角使用后的冷却时间（单位：游戏刻，20 ticks = 1秒）\n默认值：3600（3分钟）")
            .defineInRange("cooldown_ticks", 3600, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue GUARD_COUNT = BUILDER
            .comment("\n警卫召唤数量\n默认值：3")
            .defineInRange("guard_count", 3, 1,9);

    public static final ForgeConfigSpec.BooleanValue ENABLE_ZOMBIE_VS_ILLAGER = BUILDER
            .comment("\n启用僵尸与灾厄村民之间的敌对行为\n默认值：true")
            .define("enable_zombie_vs_illager", true);
    public static final ForgeConfigSpec.BooleanValue NO_ADVANCEMENTS = BUILDER
            .comment("\n无需获得“村庄英雄”进度就能使用警卫号角\n默认值：false")
            .define("no_advancements", false);
    public static  final ForgeConfigSpec.BooleanValue IS_MELEE_BACK_OFF = BUILDER
            .comment("\n警卫近战是否后撤\n默认值：true（原版行为）")
            .define("melee_backoff", true);
    public static  final  ForgeConfigSpec.ConfigValue<List<? extends  String>> ILLAGER_EXTRA_HOSTILE_TARGETS = BUILDER
            .comment("\n灾厄村民（含劫掠兽、女巫）额外敌对的生物\n填入实体注册名，如 [\"minecraft:warden\", \"minecraft:wither\"]\n默认值：空列表")
            .defineListAllowEmpty("illager_extra_hostile_targets", ArrayList::new, o -> o instanceof  String);


    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // 保存配置值的实际变量
    public static int cooldownTicks;
    public static int guardCount;
    public static boolean enableZombieVsIllager;
    public static boolean noAdvancements;
    public static boolean melee_backoff;
    public static List<? extends String> extraHostileTargets;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        // 当配置加载或重载时，将配置值赋给静态变量
        cooldownTicks = COOLDOWN_TICKS.get();
        guardCount = GUARD_COUNT.get();
        enableZombieVsIllager = ENABLE_ZOMBIE_VS_ILLAGER.get();
        noAdvancements = NO_ADVANCEMENTS.get();
        melee_backoff = IS_MELEE_BACK_OFF.get();
        extraHostileTargets = ILLAGER_EXTRA_HOSTILE_TARGETS.get();
    }
}
