package net.bobbacon2.mixin;

import net.bobbacon2.NightOfTheDead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {
    @Inject(method = "onDeath", at= @At("HEAD"),require = 1)
    private void injectOnDeath(DamageSource damageSource, CallbackInfo ci){
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        if (!self.getWorld().isClient){
            NightOfTheDead.setShouldPlayANightOfTheDead(true,(ServerWorld) self.getWorld());
        }

    }
}
