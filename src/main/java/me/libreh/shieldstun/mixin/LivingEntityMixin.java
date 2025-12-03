package me.libreh.shieldstun.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * From: TheobladTheBird/CarpetPvP @ 1.21.5 (LivingEntity_getKBFix.java)
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
            method = "getAttackKnockbackAgainst",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifyKnockback(Entity entity, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof LivingEntity target && target.timeUntilRegen < 20) {
            cir.setReturnValue(0.0F);
            return;
        }

        float baseKnockback = (float) ((LivingEntity) (Object) this).getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK);
        World world = ((LivingEntity) (Object) this).getEntityWorld();
        if (world instanceof ServerWorld serverWorld) {
            float modifiedKnockback = EnchantmentHelper.modifyKnockback(serverWorld, ((LivingEntity) (Object) this).getMainHandStack(), entity, damageSource, baseKnockback);
            cir.setReturnValue(modifiedKnockback);
        } else {
            cir.setReturnValue(baseKnockback);
        }
    }
}
