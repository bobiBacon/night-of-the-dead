package net.bobbacon.mixin.client;

import net.bobbacon.NightOfTheDeadClient;
import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",at = @At("HEAD"),argsOnly = true)
    public BakedModel parchmentRender(BakedModel value, ItemStack stack, ModelTransformationMode renderMode){
//        if (stack.isOf(ModItems.SCROLL)){
//            if (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.FIXED){
////                NightOfTheDeadClient.LOGGER.info(new ModelIdentifier(ScrollItem.getSpell(stack).symbolTextureFor2d(),"inventory").toString());
////                NightOfTheDeadClient.LOGGER.info(((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(ScrollItem.getSpell(stack).getId(),"inventory")).toString());
////                NightOfTheDeadClient.LOGGER.info(((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier("awdawd","awfdwaf","inventory")).toString());
////                return ((ItemRendererAccessor) this).getModels().getModel(ModItems.Test);
//                return value;
//            }
//            BakedModel model = ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(NightOfTheDeadClient.MOD_ID,"scroll_base_3d","inventory"));
//            NightOfTheDeadClient.LOGGER.info(model.toString());
//            NightOfTheDeadClient.LOGGER.info(((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(NightOfTheDeadClient.MOD_ID,"scroll_awda_3d","inventory")).toString());
//            return model;
//        }
        return value;
    }
}
