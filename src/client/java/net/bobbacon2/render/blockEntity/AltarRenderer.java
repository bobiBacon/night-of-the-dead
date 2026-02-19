package net.bobbacon2.render.blockEntity;

import net.bobbacon2.entity.block_entity.AltarBE;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class AltarRenderer implements BlockEntityRenderer<AltarBE> {
    private final ItemRenderer itemRenderer;

    public AltarRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(
            AltarBE entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ) {
        ItemStack stack = entity.getStack();

        if (stack.isEmpty()) return;
        light = WorldRenderer.getLightmapCoordinates(
                entity.getWorld(),
                entity.getPos().up()
        );
        matrices.push();

        matrices.translate(0.5, 1.05, 0.5);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrices.scale(0.5f, 0.5f, 0.5f);

        itemRenderer.renderItem(
                stack,
                ModelTransformationMode.FIXED,
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
    }
}
