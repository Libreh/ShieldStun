package me.libreh.shieldstun.mixin;

import me.libreh.shieldstun.config.ConfigCache;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract float getDamageBlockedAmount(ServerWorld world, DamageSource source, float amount);

	@Shadow public abstract boolean isBlocking();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Unique
	private static final ConfigCache CONFIG = ConfigManager.getConfigCache();

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageBlockedAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)F", shift = At.Shift.BEFORE), cancellable = true)
	private void shieldStun$stunHit(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (CONFIG.isStunEnabled() && this.isBlocking() &&
				source.getSource() != null && source.getSource() instanceof ServerPlayerEntity
		) {
			getDamageBlockedAmount(world, source, amount);
			cir.setReturnValue(false);
		}
	}
}