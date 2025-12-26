package net.bobbacon.render.blockEntity;

import net.bobbacon.entity.block_entity.ModBE;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlockEntityRenderers {
    public static void init(){
        BlockEntityRendererFactories.register(
                ModBE.ALTAR_BE,
                AltarRenderer::new
        );
    }
}
