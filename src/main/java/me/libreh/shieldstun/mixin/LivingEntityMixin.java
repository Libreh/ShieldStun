package me.libreh.shieldstun.mixin;

import me.libreh.shieldstun.config.ConfigCache;
import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow protected float lastDamageTaken;

	@Unique
	private boolean shieldstun$stunHit;

	@Unique
	private static final ConfigCache CONFIG = ConfigManager.getConfigCache();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At("RETURN"))
	private void shieldstun$handlePostDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (shouldApplyStun(cir.getReturnValueZ())) {
			applyStunEffect();
			return;
		}
		shieldstun$stunHit = false;
	}

	@ModifyVariable(method = "takeKnockback", at = @At("HEAD"), ordinal = 0)
	private double shieldstun$modifyKnockbackStrength(double strength) {
		if (shieldstun$shouldModifyKnockback()) {
			return strength * CONFIG.getKnockbackModifier();
		}
		return strength;
	}

	@Unique
	private boolean shouldApplyStun(boolean damageResult) {
		return !damageResult
				&& lastDamageTaken <= 0
				&& CONFIG.isStunEnabled();
	}

	@Unique
	private void applyStunEffect() {
		timeUntilRegen = 0;
		shieldstun$stunHit = true;
	}

	@Unique
	private boolean shieldstun$shouldModifyKnockback() {
		return shieldstun$stunHit
				&& CONFIG.isStunEnabled()
				&& CONFIG.isPaperShieldKnockbackEnabled();
	}
}