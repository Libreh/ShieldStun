package me.libreh.shieldstun.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends @NotNull LivingEntity> entityType, Level level) { super(entityType, level); }

    @Inject(method = "disableShield()V", at = @At("HEAD"))
    private void onDisableShield(CallbackInfo ci) {
        this.invulnerableTime = 20;
    }
}
