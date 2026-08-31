package com.ming.guardhorn.features;

import com.ming.guardhorn.config.ModCommonConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GuardHornItem extends Item {

    public GuardHornItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @Nullable LivingEntity entity) {
        return 40;
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        var hornVariants = SoundEvents.GOAT_HORN_SOUND_VARIANTS;
        SoundEvent hornSound = hornVariants.get(level.random.nextInt(hornVariants.size())).value();
        if (hornSound == null) {
            // 播放一个实体的声音作为错误提示
            hornSound = SoundEvents.GOAT_HORN_BREAK;
        }
        //服务端向附近玩家广播，客户端听到
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                hornSound, SoundSource.PLAYERS, 1.0F, 1.0F);

        player.getCooldowns().addCooldown(this, ModCommonConfig.CONFIG.getCooldownTicks());
        player.startUsingItem(hand);
        //返回 success 表示使用成功但不消耗物品
        return InteractionResultHolder.consume(stack);
    }
}
