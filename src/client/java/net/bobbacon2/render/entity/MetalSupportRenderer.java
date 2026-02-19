package net.bobbacon2.render.entity;

import net.bobbacon2.NightOfTheDeadClient;
import net.bobbacon2.entity.MetalSupport;
import net.bobbacon.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.OptionalInt;

public class MetalSupportRenderer extends ItemFrameEntityRenderer<MetalSupport> {
    private static final ModelIdentifier modelIdentifier = new ModelIdentifier(NightOfTheDeadClient.MOD_ID, "metal_support","");
    private static final Identifier TEXTURE =
            Identifier.of("minecraft","textures/item/iron_bars.png");



    public MetalSupportRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(MetalSupport itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        Direction direction = itemFrameEntity.getHorizontalFacing();
        Vec3d vec3d = this.getPositionOffset(itemFrameEntity, g);
        matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
        double d = 0.46875;
        matrixStack.translate(direction.getOffsetX() * 0.46875, direction.getOffsetY() * 0.46875, direction.getOffsetZ() * 0.46875);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(itemFrameEntity.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - itemFrameEntity.getYaw()));
        boolean bl = itemFrameEntity.isInvisible();
        ItemStack itemStack = itemFrameEntity.getHeldItemStack();
        if (!bl) {
            BakedModelManager bakedModelManager = this.blockRenderManager.getModels().getModelManager();
            ModelIdentifier modelIdentifier = this.getModelId(itemFrameEntity, itemStack);
            matrixStack.push();
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
            this.blockRenderManager
                    .getModelRenderer()
                    .render(
                            matrixStack.peek(),
                            vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntitySolid()),
                            null,
                            bakedModelManager.getModel(modelIdentifier),
                            1.0F,
                            1.0F,
                            1.0F,
                            i,
                            OverlayTexture.DEFAULT_UV
                    );
            matrixStack.pop();
        }

        if (!itemStack.isEmpty()) {



            if (itemStack.isIn(ItemTags.TOOLS)) {
                matrixStack.scale(1.5F, 1.5F, 1.0F);
                matrixStack.translate(0.0F, 0.0F, 0.325F);
                if (itemStack.isIn(ItemTags.SWORDS)){
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135));
                    matrixStack.translate(-0.25F, 0.25F, 0.0F);
                }else {
                    matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(65));
                }
            } else {
                matrixStack.translate(0.0F, 0.0F, 0.325F);
                matrixStack.scale(0.8F, 0.8F, 0.8F);
            }
            this.itemRenderer
                    .renderItem(
                            itemStack,
                            ModelTransformationMode.FIXED,
                            i,
                            OverlayTexture.DEFAULT_UV,
                            matrixStack,
                            vertexConsumerProvider,
                            itemFrameEntity.getWorld(),
                            itemFrameEntity.getId()
                    );

        }

        matrixStack.pop();
    }

    @Override
    public ModelIdentifier getModelId(MetalSupport entity, ItemStack stack) {
        return modelIdentifier;
    }
    @Override
    public Identifier getTexture(MetalSupport entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
