package net.bobbacon2.mixin.client.sodium;

import me.jellysquid.mods.sodium.client.model.color.DefaultColorProviders;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.bobbacon2.NightOfTheDeadClientManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultColorProviders.WaterColorProvider.class)
public class WaterColorMixin {
    @Inject(method = "getColor",at= @At("HEAD"), cancellable = true,remap = false)
    private void changeColor(WorldSlice world, int x, int y, int z, CallbackInfoReturnable<Integer> cir){
        if (NightOfTheDeadClientManager.isNightOfTheDead){
            cir.setReturnValue(0x850606);
        }
    }
}
