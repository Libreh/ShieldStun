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
import net.minecraft.util.math.Vec3d;
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
	private float damage(LivingEntity instance, ServerWorld world, DamageSource source, float amount, Operation<Float> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
		if (shouldStun(source)) {
			cir.setReturnValue(false);
		}
		return original.call(instance, world, source, amount);
	}

	@WrapOperation(method = "getDamageBlockedAmount", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeShieldHit(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V"))
	private void getDamageBlockedAmount(LivingEntity instance, ServerWorld world, LivingEntity attacker, Operation<Void> original) {
		if (isFacingAttacker(attacker.getPos())) {
			original.call(instance, world, attacker);
		}
	}

	@Unique
	private boolean shouldStun(DamageSource source) {
		if (source.getPosition() == null) return false;
		if (!isBlocking() || !isFacingAttacker(source.getPosition()) && isBlocking()) return false;
		if (source.getSource() == null) return false;
		if (!(source.getSource() instanceof ServerPlayerEntity)) return false;
        return ConfigManager.getConfig().enableStuns;
    }

	@Unique
	private boolean isFacingAttacker(Vec3d attackPos) {
		Vec3d facing = this.getRotationVector(0.0F, this.getHeadYaw());
		Vec3d toAttack = attackPos.subtract(this.getPos()).multiply(1, 0, 1).normalize();
		double angle = Math.acos(toAttack.dotProduct(facing));
		return angle < (Math.PI / 2);
	}
}