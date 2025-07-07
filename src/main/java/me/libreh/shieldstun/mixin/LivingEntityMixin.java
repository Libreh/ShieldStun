package me.libreh.shieldstun.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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

	@WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeShieldHit(Lnet/minecraft/entity/LivingEntity;)V"))
	private void shieldStun(LivingEntity instance, LivingEntity attacker, Operation<Void> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
		if (shouldStun(attacker)) {
			cir.setReturnValue(false);
		}
		original.call(instance, attacker);
	}

	@Unique
	private boolean shouldStun(LivingEntity attacker) {
		if (!(attacker instanceof ServerPlayerEntity)) return false;
		if (!this.isBlocking()) return false;
        return ConfigManager.getConfig().enableStuns;
    }
}