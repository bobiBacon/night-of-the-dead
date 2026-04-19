package net.bobbacon2.render.entity;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.entity.ModEntities;
import net.bobbacon2.render.model.SoulWormModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class EntityRenderers {
    public static final EntityModelLayer SOUL_WORM= new EntityModelLayer(new Identifier(NightOfTheDead.MOD_ID, "soul_worm"), "main");

    public static void init() {
        EntityRendererRegistry.register(ModEntities.MOLOTOV, MolotovRenderer::new);
        EntityRendererRegistry.register(ModEntities.NAPALM_MOLOTOV, MolotovRenderer::new);
        EntityRendererRegistry.register(ModEntities.EXPLOSIVE_MOLOTOV, MolotovRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_DROP,FireDropRenderer::new);
        EntityRendererRegistry.register(ModEntities.METAL_SUPPORT, MetalSupportRenderer::new);
        EntityRendererRegistry.register(ModEntities.SOUL_WORM,EmptyEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SOUL_WORM_PART, WormPartRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(SOUL_WORM, SoulWormModel::getTexturedModelData);
    }
}
