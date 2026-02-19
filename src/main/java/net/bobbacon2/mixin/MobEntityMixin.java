package net.bobbacon2.mixin;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.status_effect.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {




    @Inject(
            method = "getAmbientSound",
            at = @At("HEAD"),
            cancellable = true
    )
    private void CreeperAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (((MobEntity)(Object)this) instanceof CreeperEntity){
            cir.setReturnValue(SoundEvents.ENTITY_CREEPER_HURT);
        }
    }

    @Inject(method = "tryAttack",at = @At("TAIL"))
    private void boostAttack(Entity target, CallbackInfoReturnable<Boolean> cir){
        if (target.getWorld().isClient()){
            return;
        }
        if (cir.getReturnValue() && target instanceof LivingEntity && NightOfTheDead.isNightOfTheDead((ServerWorld) target.getWorld())){
            MobEntity self = (MobEntity) (Object) this;
            float f = self.getWorld().getLocalDifficulty(self.getBlockPos()).getLocalDifficulty();
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(ModEffects.ATTRITION, 200 * (int)f, (int) f), self);
        }
    }
}
