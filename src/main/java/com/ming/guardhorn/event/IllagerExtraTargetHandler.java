package com.ming.guardhorn.event;

import com.ming.guardhorn.config.ModCommonConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IllagerExtraTargetHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IllagerExtraTargetHandler.class);

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        EntityType<?> type = mob.getType();
        if (!(mob instanceof AbstractIllager) && type != EntityType.RAVAGER && type != EntityType.WITCH) {
            return;
        }

        // 攻击配置列表中的额外生物（优先级 3，低于僵尸 vs 灾厄村民的优先级 2）
        Set<EntityType<?>> extraTargets = parseExtraTargets(ModCommonConfig.CONFIG.getIllagerExtraHostileTargets());
        if (!extraTargets.isEmpty()) {
            mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                    mob,
                    LivingEntity.class,      // 目标类型为所有活体，配合谓词过滤
                    10,                      // 扫描间隔（刻）
                    true,                    // 必须看到目标
                    false,                   // 不检查是否在视野内
                    entity -> extraTargets.contains(entity.getType())
            ));
        }
    }

    // 将配置的字符串 ID 列表解析为 EntityType 集合；无效 ID 跳过并告警，空结果返回空集合
    private Set<EntityType<?>> parseExtraTargets(List<? extends String> rawIds) {
        if (rawIds == null || rawIds.isEmpty()) {
            return Set.of();
        }

        Set<EntityType<?>> result = new HashSet<>();
        for (String id : rawIds) {
            if (id == null || id.isBlank()) continue;

            ResourceLocation rl = ResourceLocation.tryParse(id);
            if (rl == null) {
                LOGGER.warn("配置的实体 ID 格式无效，已跳过: '{}'", id);
                continue;
            }

            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(rl);
            if (type == null) {
                LOGGER.warn("找不到对应的实体类型，已跳过: '{}'", id);
                continue;
            }

            result.add(type);
        }

        return result;
    }
}
