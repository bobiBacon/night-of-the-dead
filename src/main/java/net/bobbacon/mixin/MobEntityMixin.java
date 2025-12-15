package net.bobbacon.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
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
}
