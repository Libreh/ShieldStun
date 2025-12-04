package me.libreh.shieldstun.mixin;

import me.libreh.shieldstun.config.ConfigManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * From: TheobaldTheBird/CarpetPvP @ 1.21.5 (Player_shieldStunMixin.java)
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"))
    private void onShieldDisabled(ServerWorld serserverWorlderLevel, LivingEntity livingEntity, CallbackInfo ci) {
        var canDisableShield = false;
        // same code as from blockUsingItem in LivingEntity where it checks if you can disable shield
        ItemStack itemStack = this.getBlockingItem();
        BlocksAttacksComponent blocksAttacks = itemStack != null ? (BlocksAttacksComponent)itemStack.get(DataComponentTypes.BLOCKS_ATTACKS) : null;
        float f = livingEntity.getWeaponDisableBlockingForSeconds();
        if (f > 0.0F && blocksAttacks != null) {
            canDisableShield = true;
        }

        if (canDisableShield && ConfigManager.getConfig().enableStuns) {
            this.timeUntilRegen = 20;
            executor.schedule(() -> {
                this.timeUntilRegen = 0;
            }, 1, TimeUnit.MILLISECONDS);
        }
    }
}
