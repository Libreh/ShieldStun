package me.libreh.shieldstun.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.libreh.shieldstun.api.ShieldStunHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean blockedHit = false;

    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float hurtServer(LivingEntity instance, ServerLevel serverLevel, DamageSource damageSource, float damageAmount, Operation<Float> original) {
        float blockedAmount = original.call(instance, serverLevel, damageSource, damageAmount);
        blockedHit = ShieldStunHelper.isEnabled() && blockedAmount != 0.0F;
        return blockedAmount;
    }

    @ModifyReturnValue(method = "hurtServer", at = @At("RETURN"))
    private boolean hurtServerReturn(boolean original) {
        if (blockedHit) {
            this.invulnerableTime = 0;
            return false;
        }
        return original;
    }
}
