package net.bobbacon.mixin.client;

import net.bobbacon.NightOfTheDeadClient;
import net.bobbacon.item.ModItems;
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
//        if (stack.isOf(ModItems.SCROLL)&& renderMode != ModelTransformationMode.GUI){
//            return ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(NightOfTheDeadClient.MOD_ID,"scroll_3d","inventory"));
//        }
        return value;
    }
}
