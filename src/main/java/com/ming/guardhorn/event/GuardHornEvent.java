package com.ming.guardhorn.event;

import com.ming.guardhorn.config.ModCommonConfig;
import com.ming.guardhorn.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;
import tallestegg.guardvillagers.GuardEntityType;
import tallestegg.guardvillagers.common.entities.Guard;

public class GuardHornEvent {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (event.getLevel().isClientSide()) return;
        if (event.getItemStack().getItem() != ModItems.GUARD_HORN.get()) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ServerLevel level = serverPlayer.serverLevel();
        if (!ModList.get().isLoaded("guardvillagers")) {
            LOGGER.info("未找到 GuardVillagers 模组！");
            return;
        }

        EntityType<Guard> guard_type = GuardEntityType.GUARD.get();

        if (!ModCommonConfig.CONFIG.isNoAdvancements()) {
            ResourceLocation advId = ResourceLocation.fromNamespaceAndPath("minecraft", "adventure/hero_of_the_village");

            AdvancementHolder advancement = serverPlayer.server.getAdvancements().get(advId);
            if (advancement == null) {
                LOGGER.warn("未找到进度：adventure/hero_of_the_village");
                return;
            }

            AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
                serverPlayer.sendSystemMessage(Component.literal("请先成功一次抵御袭击后再使用"), false);
                LOGGER.info("玩家 {} 尚未完成村庄英雄进度，无效果。", serverPlayer.getName().getString());
                return;
            }
        }

        serverPlayer.addEffect(new MobEffectInstance(
                MobEffects.HERO_OF_THE_VILLAGE,
                -1,
                0,
                false,
                true,
                true
        ));

        int guardCount = ModCommonConfig.CONFIG.getGuardCount();
        for (int i = 0; i < guardCount; i++) {
            double x = player.getX() + (level.random.nextDouble() - 0.5) * 2;
            double y = player.getY();
            double z = player.getZ() + (level.random.nextDouble() - 0.5) * 2;

            Guard guard0 = guard_type.spawn(level,
                    (ItemStack) null,
                    null,
                    BlockPos.containing(x, y, z),
                    MobSpawnType.SPAWN_EGG,
                    false,
                    false);

            if (guard0 != null) {
                guard0.setOwnerId(player.getUUID());
                guard0.setFollowing(true);
            } else {
                LOGGER.warn("守卫生成失败");
            }
        }
        LOGGER.info("生成 {} 个警卫村民", guardCount);
    }
}
