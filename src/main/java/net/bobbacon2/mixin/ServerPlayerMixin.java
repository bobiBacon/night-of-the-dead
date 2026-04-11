package net.bobbacon2.mixin;

import net.bobbacon2.NightOfTheDead;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {
    @Inject(method = "onDeath", at= @At("HEAD"),require = 1)
    private void injectOnDeath(DamageSource damageSource, CallbackInfo ci){
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        if (!self.getWorld().isClient){
            NightOfTheDead.addDeathToCount(((ServerWorld) self.getWorld()).getServer());
        }

    }
    @Inject(
            method = "moveToWorld",
            at = @At("TAIL")
    )
    private void onChangeDimension(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(NightOfTheDead.isNightOfTheDead(destination));
        ServerPlayNetworking.send(player, NightOfTheDead.NIGHT_OF_THE_DEAD_PACKET, buf);

    }
}
