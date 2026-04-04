package net.bobbacon2.render.fluids;

import net.bobbacon2.NightOfTheDeadClient;
import net.bobbacon2.NightOfTheDeadClientManager;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class ColoredWaterRenderHandler extends SimpleFluidRenderHandler {
    public ColoredWaterRenderHandler(Identifier stillTexture, Identifier flowingTexture, @Nullable Identifier overlayTexture, int tint) {
        super(stillTexture, flowingTexture, overlayTexture, tint);
    }

    public ColoredWaterRenderHandler(Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture) {
        super(stillTexture, flowingTexture, overlayTexture);
    }

    public ColoredWaterRenderHandler(Identifier stillTexture, Identifier flowingTexture, int tint) {
        super(stillTexture, flowingTexture, tint);
    }

    public ColoredWaterRenderHandler(Identifier stillTexture, Identifier flowingTexture) {
        super(stillTexture, flowingTexture);
    }

    @Override
    public int getFluidColor(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
        if (NightOfTheDeadClientManager.isNightOfTheDead){
            return 0xFF3300;
        }
        return super.getFluidColor(view, pos, state);
    }
}
