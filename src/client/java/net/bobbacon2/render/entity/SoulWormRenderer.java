package net.bobbacon2.render.entity;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.entity.SoulWorm;
import net.bobbacon2.entity.SoulWormPart;
import net.bobbacon2.render.model.SoulWormModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

public class SoulWormRenderer extends EntityRenderer<SoulWorm> {
    private final SoulWormModel model;

    protected SoulWormRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model= new SoulWormModel(ctx.getPart(EntityRenderers.SOUL_WORM));
    }

    @Override
    public Identifier getTexture(SoulWorm entity) {
        return new Identifier(NightOfTheDead.MOD_ID,"textures/entity/soul_worm.png");
    }
    @Override
    public void render(
            SoulWorm entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(-entity.getX(),-entity.getY(),-entity.getZ());

        List<SoulWormPart> parts = entity.getParts();

        for (SoulWormPart part : parts) {
            matrices.push();

            double x = MathHelper.lerp(tickDelta, part.prevX, part.getX());
            double y = MathHelper.lerp(tickDelta, part.prevY, part.getY());
            double z = MathHelper.lerp(tickDelta, part.prevZ, part.getZ());

            matrices.translate(part.getX(), part.getY(), part.getZ());

//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
//                    MathHelper.lerp(tickDelta, part.prevYaw, part.getYaw())
//            ));

            float scale = part.getScale();
            matrices.scale(scale, scale, scale);

            VertexConsumer consumer = vertexConsumers.getBuffer(model.getLayer(getTexture(entity)));

            model.setAngles(part, 0, 0, entity.age + tickDelta, 0, 0);
            model.render(
                    matrices,
                    consumer,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    1f, 1f, 1f, 1f
            );

            matrices.pop();
        }
        matrices.pop();
    }
}
