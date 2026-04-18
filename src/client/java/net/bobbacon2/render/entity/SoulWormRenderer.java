package net.bobbacon2.render.entity;

import net.bobbacon2.entity.SoulWorm;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SoulWormRenderer extends EntityRenderer<SoulWorm> {
    protected SoulWormRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SoulWorm entity) {
        return null;
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

        EntityRendererFactory.Context context = this.;
        WormSegmentModel segmentModel = new WormSegmentModel(context.getPart(ModModelLayers.WORM_SEGMENT));

        List<SoulWormPart> parts = entity.getParts();

        for (SoulWormPart part : parts) {
            matrices.push();

            double x = MathHelper.lerp(tickDelta, part.prevX, part.getX()) - entityRenderDispatcher.camera.getPos().x;
            double y = MathHelper.lerp(tickDelta, part.prevY, part.getY()) - entityRenderDispatcher.camera.getPos().y;
            double z = MathHelper.lerp(tickDelta, part.prevZ, part.getZ()) - entityRenderDispatcher.camera.getPos().z;

            matrices.translate(x, y, z);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                    MathHelper.lerp(tickDelta, part.prevYaw, part.getYaw())
            ));

            float scale = part.getScale();
            matrices.scale(scale, scale, scale);

            VertexConsumer consumer = vertexConsumers.getBuffer(segmentModel.getLayer(getTexture(entity)));

            segmentModel.setAngles(part, 0, 0, entity.age + tickDelta, 0, 0);
            segmentModel.render(
                    matrices,
                    consumer,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    1f, 1f, 1f, 1f
            );

            matrices.pop();
        }
    }
}
