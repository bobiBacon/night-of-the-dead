package net.bobbacon.mixin.client;

import net.bobbacon.NightOfTheDeadClient;
import net.bobbacon.NightOfTheDeadClientManager;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class FluidRendererMixin {
    @Inject(method = "getWaterColor",at= @At("HEAD"), cancellable = true)
    private static void changeColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        if (NightOfTheDeadClientManager.isNightOfTheDead){
            cir.setReturnValue(0xFF3300);
        }
    }
}
