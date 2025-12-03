package me.libreh.shieldstun.mixin;

import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

    @Inject(method = "takeShieldHit", at = @At("HEAD"))
    private void onShieldDisabled(LivingEntity livingEntity, CallbackInfo ci) {
        if (livingEntity.disablesShield() && ConfigManager.getConfig().enableStuns)
        {
            executor.schedule(() -> {
                this.timeUntilRegen = 0;
            }, 1, TimeUnit.MILLISECONDS);
        }
    }
}
