package me.libreh.shieldstun.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract boolean isBlocking();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageBlockedAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)F"))
	private float shieldStun(LivingEntity instance, ServerWorld world, DamageSource source, float amount, Operation<Float> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
		if (shouldStun(source)) {
			cir.setReturnValue(false);
		}
		return original.call(instance, world, source, amount);
	}

	@Unique
	private boolean shouldStun(DamageSource source) {
		if (!this.isBlocking()) return false;
		if (source.getSource() == null) return false;
		if (!(source.getSource() instanceof ServerPlayerEntity)) return false;
        return ConfigManager.getConfig().enableStuns;
    }
}