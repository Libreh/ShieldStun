package me.libreh.shieldstun.mixin;

import me.libreh.shieldstun.ModInit;
import me.libreh.shieldstun.config.ConfigCache;
import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract boolean damage(ServerWorld world, DamageSource source, float amount);

	@Shadow public abstract boolean isBlocking();

	@Shadow public abstract float getDamageBlockedAmount(ServerWorld world, DamageSource source, float amount);

	@Shadow public abstract @Nullable ItemStack getBlockingItem();

	@Shadow protected abstract void takeShieldHit(ServerWorld world, LivingEntity attacker);

	@Shadow public abstract Hand getActiveHand();

	@Unique
	private static final ConfigCache CONFIG = ConfigManager.getConfigCache();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;getDamageBlockedAmount(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)F",
					shift = At.Shift.BEFORE
			),
			cancellable = true
	)
	private void shieldStun$stunHit(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (CONFIG.isStunEnabled() && this.isBlocking() && source.getSource() != null) {
			breakShieldNoDamage(world, source, amount);
			cir.setReturnValue(false);
		}
	}

	@Unique
	private void breakShieldNoDamage(ServerWorld world, DamageSource source, float amount) {
		ItemStack itemStack = this.getBlockingItem();
		if (itemStack != null) {
			BlocksAttacksComponent blocksAttacksComponent = itemStack.get(DataComponentTypes.BLOCKS_ATTACKS);
			if (blocksAttacksComponent != null && !(Boolean)blocksAttacksComponent.bypassedBy().map(source::isIn).orElse(false)) {
				if (!(source.getSource() instanceof PersistentProjectileEntity persistentProjectileEntity && persistentProjectileEntity.getPierceLevel() > 0)) {
					Vec3d vec3d = source.getPosition();
					double d;
					if (vec3d != null) {
						Vec3d vec3d2 = this.getRotationVector(0.0F, this.getHeadYaw());
						Vec3d vec3d3 = vec3d.subtract(this.getPos());
						vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z).normalize();
						d = Math.acos(vec3d3.dotProduct(vec3d2));
					} else {
						d = (float) Math.PI;
					}

					float f = blocksAttacksComponent.getDamageReductionAmount(source, amount, d);
					blocksAttacksComponent.onShieldHit(this.getWorld(), itemStack, (LivingEntity) (Object) this, this.getActiveHand(), f);
					if (!source.isIn(DamageTypeTags.IS_PROJECTILE) && source.getSource() instanceof LivingEntity livingEntity) {
						this.takeShieldHit(world, livingEntity);
					}
				}
			}
		}
	}
}