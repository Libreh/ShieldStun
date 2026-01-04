package me.libreh.shieldstun.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract float getYHeadRot();

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float hurtServer(LivingEntity instance, ServerLevel world, DamageSource source, float amount, Operation<Float> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        float blockedDamage = original.call(instance, world, source, amount);
        if (blockedDamage != 0.0F && ConfigManager.getConfig().enableStuns) {
            cir.setReturnValue(false);
        }
        return blockedDamage;
    }
}