package net.bobbacon2.render.entity;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.entity.SoulWormPart;
import net.bobbacon2.render.model.SoulWormModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class WormPartRenderer extends MobEntityRenderer<SoulWormPart, SoulWormModel> {
    public WormPartRenderer(EntityRendererFactory.Context context) {
        super(context, new SoulWormModel(context.getPart(EntityRenderers.SOUL_WORM)), 1);
    }

    @Override
    public Identifier getTexture(SoulWormPart entity) {
        return new Identifier(NightOfTheDead.MOD_ID,"textures/entity/soul_worm.png");
    }
}
