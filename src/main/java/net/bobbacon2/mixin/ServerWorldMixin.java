package net.bobbacon2.mixin;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.NightOfTheDeadManager;
import net.bobbacon.ritual.RitualManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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
        if (!world.getRegistryKey().equals(World.OVERWORLD)){
            return;
        }
        long time = world.getTimeOfDay();

        if (time == 13000&&NightOfTheDead.ShouldPlayANightOfTheDead(world)&&!NightOfTheDead.isNightOfTheDead(world)) {
            NightOfTheDead.setNightOfTheDead(true,world.getServer());
            NightOfTheDead.setShouldPlayANightOfTheDead(false,world.getServer());
            wakeSleepingPlayers();

            world.getServer().getPlayerManager().broadcast(Text.of("All deaths must be punished."),false);
        }else
        if (time<13000&& NightOfTheDead.isNightOfTheDead(world)) {
            NightOfTheDead.setNightOfTheDead(false,world.getServer());
        }
        RitualManager.get(world).tick();

    }
}
