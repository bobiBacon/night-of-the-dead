package net.bobbacon.render.item;

import net.bobbacon.NightOfTheDeadClient;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellType;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ScrollItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        SpellType<?> spell = ScrollItem.getSpell(stack);
//        NightOfTheDead.LOGGER.warn("rendering scroll");
        MinecraftClient client = MinecraftClient.getInstance();
        if (mode == ModelTransformationMode.GUI){
//
            client.getItemRenderer().renderBakedItemModel(
                    client.getItemRenderer().getModels().getModelManager().getModel(new ModelIdentifier(NightOfTheDeadClient.MOD_ID,"scroll_2d","inventory")),
                    stack,
                    255,
                    overlay,
                    matrices,
                    vertexConsumers.getBuffer(RenderLayers.getItemLayer(stack, true))

            );
        }else {

            BakedModel model=client.getItemRenderer().getModels().getModelManager().getModel(new ModelIdentifier(NightOfTheDeadClient.MOD_ID,"scroll_3d","inventory"));
            model.getTransformation()
                    .getTransformation(mode)
                    .apply(false, matrices);
            matrices.translate(0F, 0.F, 0.5F);
            client.getItemRenderer().renderBakedItemModel(
                    model,
                    stack,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers.getBuffer(RenderLayers.getItemLayer(stack, true)));
        }


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
                .apply(spell.symbolTextureFor2d());

        VertexConsumer consumer = vertices.getBuffer(
                RenderLayer.getEntityCutoutNoCull(sprite.getAtlasId())
        );

        matrices.push();

        matrices.translate(0.4, 0.4, 0.8);
//        matrices.scale(1f, 1f, 1f);
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90));

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;

        consumer.vertex(matrix, -0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, -0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        matrices.pop();
    }
}
