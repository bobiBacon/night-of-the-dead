package net.bobbacon2.render.blockEntity;

import net.bobbacon2.entity.block_entity.ModBE;
import net.bobbacon2.entity.block_entity.ModBE;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlockEntityRenderers {
    public static void init(){
        BlockEntityRendererFactories.register(
                ModBE.ALTAR_BE,
                AltarRenderer::new
        );
        BlockEntityRendererFactories.register(
                ModBE.BLOOD_POOL,
                BloodPoolRenderer::new
        );
    }
}
