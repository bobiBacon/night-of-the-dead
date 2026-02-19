package net.bobbacon2.render.entity;

import net.bobbacon2.entity.ModEntities;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.registry.Registries;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.registry.Registry;

public class EntityRenderers {

    public static void init() {
        EntityRendererRegistry.register(ModEntities.MOLOTOV, MolotovRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_DROP,FireDropRenderer::new);
        EntityRendererRegistry.register(ModEntities.METAL_SUPPORT, MetalSupportRenderer::new);
    }
}
