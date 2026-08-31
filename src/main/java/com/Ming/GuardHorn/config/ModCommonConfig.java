package com.ming.guardhorn.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * record 风格：record 持有各个 {@link ModConfigSpec.ConfigValue}，
 * 其值会随配置文件的加载 / 重载自动更新，不需要再手动缓存或监听 {@code ModConfigEvent}。
 */
public record ModCommonConfig(
        ModConfigSpec.IntValue cooldownTicks,
        ModConfigSpec.IntValue guardCount,
        ModConfigSpec.BooleanValue enableZombieVsIllager,
        ModConfigSpec.BooleanValue noAdvancements,
        ModConfigSpec.BooleanValue meleeBackoff,
        ModConfigSpec.ConfigValue<List<? extends  String>> illagerExtraHostileTargets
) {
    public static final ModCommonConfig CONFIG;
    public static final ModConfigSpec SPEC;

    static {
        var pair = new ModConfigSpec.Builder().configure(ModCommonConfig::build);
        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }

    private static ModCommonConfig build(ModConfigSpec.Builder builder) {
        return new ModCommonConfig(
                builder
                        .comment("号角使用后的冷却时间（单位：游戏刻，20 ticks = 1秒）\n默认值：3600（3分钟）")
                        .defineInRange("cooldown_ticks", 3600, 0, Integer.MAX_VALUE),
                builder
                        .comment("\n警卫召唤数量\n默认值：3")
                        .defineInRange("guard_count", 3, 1, 9),
                builder
                        .comment("\n启用僵尸与灾厄村民之间的敌对行为\n默认值：true")
                        .define("enable_zombie_vs_illager", true),
                builder
                        .comment("\n无需获得“村庄英雄”进度就能使用警卫号角\n默认值：false")
                        .define("no_advancements", false),
                builder
                        .comment("\n警卫近战是否后撤\n默认值：true（原版行为）")
                        .define("melee_backoff", true),
                builder
                        .comment("\n灾厄村民（含劫掠兽、女巫）额外敌对的生物\n填入实体注册名，如 [\"minecraft:warden\", \"minecraft:wither\"]\n默认值：空列表")
                        .defineListAllowEmpty("illager_extra_hostile_targets", ArrayList::new, o -> o instanceof  String)
        );
    }

    public int getCooldownTicks() {
        return cooldownTicks.get();
    }

    public int getGuardCount() {
        return guardCount.get();
    }

    public boolean isEnableZombieVsIllager() {
        return enableZombieVsIllager.get();
    }

    public boolean isNoAdvancements() {
        return noAdvancements.get();
    }
    public boolean isMeleeBackoff() {
        return meleeBackoff.get();
    }

    public List<? extends String> getIllagerExtraHostileTargets() {
        return illagerExtraHostileTargets.get();
    }
}
