package net.bobbacon2.render.blockEntity;

import net.bobbacon2.entity.block_entity.BloodPool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BloodPoolRenderer implements BlockEntityRenderer<BloodPool> {
    public BloodPoolRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(BloodPool entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getLevel()==0){
            return;
        }
        matrices.push();

        float height = entity.getLevel() /3f *(6f/16)-0.05f;
        float wave = MathHelper.sin((MinecraftClient.getInstance().world.getTime() + tickDelta) * 0.1f) * 0.01f;
        height += wave;

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());

        Identifier textureId = new Identifier("minecraft", "block/water_still");

        Sprite sprite = MinecraftClient.getInstance()
                .getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(textureId);

        // Quad (surface du sang)
        Matrix3f normalMatrix = matrices.peek().getNormalMatrix();
        buffer.vertex(matrix, 2f/16, height, 2f/16).color(120, 0, 0, 255).texture(sprite.getMinU(), sprite.getMinV()).light(light).normal(normalMatrix,0,1,0).next();
        buffer.vertex(matrix, 2f/16, height, 14f/16).color(120, 0, 0, 255).texture(sprite.getMinU(), sprite.getMaxV()).light(light).normal(normalMatrix,0,1,0).next();
        buffer.vertex(matrix, 14f/16, height, 14f/16).color(120, 0, 0, 255).texture(sprite.getMaxU(),  sprite.getMaxV()).light(light).normal(normalMatrix,0,1,0).next();
        buffer.vertex(matrix, 14f/16, height, 2f/16).color(120, 0, 0, 255).texture(sprite.getMaxU(),  sprite.getMinV()).light(light).normal(normalMatrix,0,1,0).next();

        matrices.pop();
    }
}
