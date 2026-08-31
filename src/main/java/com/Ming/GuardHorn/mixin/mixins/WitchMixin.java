package com.Ming.GuardHorn.mixin.mixins;

import com.Ming.GuardHorn.GuardHornMod;
import com.Ming.GuardHorn.config.ModCommonConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Witch.class)
public class WitchMixin {
    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void guardhorn$throwHealingAtZombies(LivingEntity target, float velocity, CallbackInfo ci) {

        // 功能开关关闭，或目标不是僵尸时，交给原版逻辑
        if (!ModCommonConfig.enableWitchMixin) return;
        if (!(target instanceof Zombie)) return;



        Witch witch = (Witch) (Object) this;
        // 与原版一致：正在喝药水时不进行远程攻击
        if (witch.isDrinkingPotion()) return;

        Vec3 delta = target.getDeltaMovement();
        double dx = target.getX() + delta.x - witch.getX();
        double dy = target.getEyeY() - 1.1F - witch.getY();
        double dz = target.getZ() + delta.z - witch.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        ItemStack potionStack = new ItemStack(Items.SPLASH_POTION);
        PotionUtils.setPotion(potionStack, Potions.STRONG_HEALING);
        ThrownPotion thrownPotion = new ThrownPotion(witch.level(), witch);
        thrownPotion.setItem(potionStack);
        thrownPotion.setXRot(thrownPotion.getXRot() + 20.0F);
        thrownPotion.shoot(dx, dy + dist * 0.2, dz, 0.75F, 8.0F);

        if (!witch.isSilent()) {
            witch.level()
                    .playSound(
                            null,
                            witch.getX(),
                            witch.getY(),
                            witch.getZ(),
                            SoundEvents.WITCH_THROW,
                            witch.getSoundSource(),
                            1.0F,
                            0.8F + witch.getRandom().nextFloat() * 0.4F
                    );
        }

        witch.level().addFreshEntity(thrownPotion);
        ci.cancel();
    }
}
