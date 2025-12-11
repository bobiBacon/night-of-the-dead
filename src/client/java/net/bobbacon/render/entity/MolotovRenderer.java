package net.bobbacon.render.entity;

import net.bobbacon.entity.MolotovEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class MolotovRenderer extends FlyingItemEntityRenderer<MolotovEntity> {
    public MolotovRenderer(EntityRendererFactory.Context context) {
        super(context);
    }



}
