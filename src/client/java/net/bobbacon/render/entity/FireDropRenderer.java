package net.bobbacon.render.entity;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.entity.FireDrop;
import net.bobbacon.entity.MolotovEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class FireDropRenderer extends EntityRenderer<FireDrop> {
    private static final Identifier TEXTURE = Identifier.of(NightOfTheDead.MOD_ID,"textures/entity/fire_drop.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);

    public FireDropRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(FireDrop entity) {
        return TEXTURE;
    }
    @Override
    public void render(FireDrop fireDrop, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(1.0F, 1.0F, 1.0F);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, 0);
        matrixStack.pop();
        super.render(fireDrop, f, g, matrixStack, vertexConsumerProvider, i);
    }
    private static void produceVertex(
            VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV
    ) {
        vertexConsumer.vertex(positionMatrix, x - 0.5F, y - 0.25F, 0.0F)
                .color(255, 255, 255, 255)
                .texture(textureU, textureV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }


}
