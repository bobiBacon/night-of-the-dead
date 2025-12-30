package net.bobbacon.render.item;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bobbacon.NightOfTheDead;
import net.bobbacon.NightOfTheDeadClient;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellType;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ScrollItemRenderer {

    public static void renderSpellSymbolGui(
            ItemStack stack,
            MatrixStack matrices,
            VertexConsumerProvider consumers,
            int overlay
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        SpellType<?> spell = ScrollItem.getSpell(stack);
        if (spell == null || spell.isEmpty()) return;
        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(spell.symbolTextureFor2d());


        matrices.push();

        // On se place AU-DESSUS de l'item
        matrices.translate(-0.1f, -0.1f, 1);
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90));

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        VertexConsumer vc = consumers.getBuffer(
                RenderLayer.getEntityCutoutNoCull(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        );

        int light = 0xF000F0; // FULL BRIGHT

        float min = 4f;
        float max = 12f;
        MatrixStack.Entry entry = matrices.peek();
        Matrix3f normal = entry.getNormalMatrix();

        vc.vertex(matrix, -0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        vc.vertex(matrix, 0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        vc.vertex(matrix, 0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        vc.vertex(matrix, -0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        matrices.pop();
    }

    public static void renderSymbol(
            ItemStack stack,
            MatrixStack matrices,
            VertexConsumerProvider vertices,
            int light,
            boolean leftHanded
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        SpellType<?> spell = ScrollItem.getSpell(stack);
        if (spell == null || spell.isEmpty()) return;


        Sprite sprite = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(spell.symbolTexture());

        VertexConsumer consumer = vertices.getBuffer(
                RenderLayer.getEntityCutoutNoCull(sprite.getAtlasId())
        );

        matrices.push();


        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(25));

        if (leftHanded){
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(25));

        }
        else {
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(25));

        }
        matrices.translate(0, 0.4, 0.3);
        matrices.scale(0.4f,0.4f,0.4f);



        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();

        int overlay = OverlayTexture.DEFAULT_UV;

        consumer.vertex(matrix, -0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, -0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, 0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        consumer.vertex(matrix, -0.5f, 0.5f, 0)
                .color(255, 255, 255, 255)
                .texture(sprite.getMinU(), sprite.getMinV())
                .overlay(overlay)
                .light(light)
                .normal(normal, 0, 0, 1)
                .next();

        matrices.pop();
    }
}
