package net.bobbacon.mixin;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.NightOfTheDeadManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow protected abstract void wakeSleepingPlayers();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onWorldTick(CallbackInfo ci) {

        ServerWorld world = (ServerWorld) (Object) this;

        long time = world.getTimeOfDay();

        if (time == 13000) {
            NightOfTheDead.setNightOfTheDead(true,world);
            NightOfTheDead.setShouldPlayANightOfTheDead(false,world);
            wakeSleepingPlayers();

            world.getServer().getPlayerManager().broadcast(Text.of("All death must be punished."),false);
        }
        if (world.isDay()&& NightOfTheDead.isNightOfTheDead(world)) {
            NightOfTheDead.setNightOfTheDead(false,world);
        }

    }
}
