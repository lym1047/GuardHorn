package com.Ming.GuardHorn.event;

import com.Ming.GuardHorn.config.ModCommonConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class ZombieHitIllagers {

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (!ModCommonConfig.enableZombieVsIllager) {
            return;
        }

        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        EntityType<?> type = mob.getType();

        // 所有类型僵尸攻击所有灾厄村民、劫掠兽和女巫
        if (mob instanceof Zombie) {
            mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, LivingEntity.class, 10, true, false,
                    this::isIllagerRavagerOrWitch
            ));
        }
        // 灾厄村民（包括劫掠兽）攻击僵尸
        else if (mob instanceof AbstractIllager || type == EntityType.RAVAGER) {
            mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Zombie.class, true));
        }
        // 女巫攻击僵尸
        else if (type == EntityType.WITCH) {
            mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Zombie.class, true));
        }
    }

    // 判断目标是否为灾厄村民、劫掠兽或女巫
    private boolean isIllagerRavagerOrWitch(LivingEntity entity) {
        return entity instanceof AbstractIllager
                || entity.getType() == EntityType.RAVAGER
                || entity.getType() == EntityType.WITCH;
    }
}
