package me.libreh.shieldstun.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected float lastHurt;

    @Unique
    private boolean blockedHit = false;

    @Unique
    private boolean stunnedBlock = false;

    @Unique
    private int invulnerableTimeBefore = 0;

    @Unique
    private float lastHurtBefore = 0.0F;

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void beforeHurt(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        blockedHit = false;
        invulnerableTimeBefore = this.invulnerableTime;
        lastHurtBefore = this.lastHurt;
    }

    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float hurtServer(LivingEntity instance, ServerLevel serverLevel, DamageSource damageSource, float damageAmount, Operation<Float> original) {
        float blockedAmount = original.call(instance, serverLevel, damageSource, damageAmount);
        float damageAfterBlocking = damageAmount - blockedAmount;
        blockedHit = ConfigManager.getConfig().enableStuns && blockedAmount != 0.0F;

        stunnedBlock = blockedHit
            && instance instanceof Player
            && damageAfterBlocking <= 0.0F
            && !absorbedByIFrames(damageSource, damageAfterBlocking);
        return blockedAmount;
    }

    @Unique
    private boolean absorbedByIFrames(DamageSource source, float damageAfterBlocking) {
        return this.invulnerableTime > 10.0F
            && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)
            && damageAfterBlocking <= this.lastHurt;
    }

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void skipDamageTick(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (stunnedBlock) {
            this.invulnerableTime = invulnerableTimeBefore;
            this.lastHurt = lastHurtBefore;
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "applyItemBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;blockUsingItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void skipShieldDisableWithinIFrames(LivingEntity instance, ServerLevel level, LivingEntity attacker, Operation<Void> original, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float damage, @Local(ordinal = 1) float damageBlocked) {
        if (ConfigManager.getConfig().enableStuns && absorbedByIFrames(source, damage - damageBlocked)) {
            return;
        }
        original.call(instance, level, attacker);
    }
}
