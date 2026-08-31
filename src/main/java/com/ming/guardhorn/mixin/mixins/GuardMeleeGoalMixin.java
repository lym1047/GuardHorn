package com.ming.guardhorn.mixin.mixins;
import com.ming.guardhorn.config.ModCommonConfig;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tallestegg.guardvillagers.common.entities.Guard;

@Mixin(Guard.GuardMeleeGoal.class)
public abstract class GuardMeleeGoalMixin {
    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/control/MoveControl;strafe(FF)V")
    )
    private void guardhorn$redirectMeleeBackoff(MoveControl control, float forward, float strafe) {
        if (ModCommonConfig.CONFIG.isMeleeBackoff()) {
            control.strafe(forward, strafe);    // 默认行为：保持后撤
        }
        // else：什么都不做，不后撤，贴脸打
    }
}
