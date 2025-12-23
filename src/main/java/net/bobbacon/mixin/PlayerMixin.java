package net.bobbacon.mixin;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.status_effect.ModEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin  {
    @Inject(method = "onDeath", at= @At("HEAD"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci){
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self.getWorld().isClient) {
            return;
        }
        NightOfTheDead.setShouldPlayANightOfTheDead(true,(ServerWorld) self.getWorld());
    }

    @Inject(method = "tick", at= @At("TAIL"))
    private void injectTick(CallbackInfo ci){
        PlayerEntity self= (PlayerEntity) (Object) this;
        if (!self.getWorld().isClient()&&(self.age&15)==0) {
            if (self.isTouchingWater()&&NightOfTheDead.isNightOfTheDead((ServerWorld) self.getWorld())){
                self.addStatusEffect(new StatusEffectInstance(ModEffects.INSANITY,200,0));
            }
        }
    }



}
