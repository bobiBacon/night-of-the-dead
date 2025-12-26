package net.bobbacon.render.item;

import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellType;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;

public class ScrollItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        SpellType<?> spell = ScrollItem.getSpell(stack);

        MinecraftClient client = MinecraftClient.getInstance();

        client.getItemRenderer().renderItem(
                stack,
                mode,
                light,
                overlay,
                matrices,
                vertexConsumers,
                client.world,
                0
        );

        if (spell != null) {
            renderSymbol(spell, matrices, vertexConsumers, light);
        }
    }
    private void renderSymbol(
            SpellType<?> spell,
            MatrixStack matrices,
            VertexConsumerProvider vertices,
            int light
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(spell.symbolTexture());

        VertexConsumer consumer = vertices.getBuffer(RenderLayer.getCutout());

        matrices.push();

        // Position sur le parchemin (à ajuster)
        matrices.translate(0.5, 0.5, 0.01);
//        matrices.scale(0.5f, 0.5f, 0.5f);

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();

        consumer.vertex(matrix, -0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .light(light)
                .next();

        consumer.vertex(matrix, 0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .light(light)
                .next();

        consumer.vertex(matrix, 0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .light(light)
                .next();

        consumer.vertex(matrix, -0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .light(light)
                .next();

        matrices.pop();
    }
}
