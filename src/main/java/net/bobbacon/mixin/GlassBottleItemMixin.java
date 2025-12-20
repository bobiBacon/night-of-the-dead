package net.bobbacon.mixin;

import net.bobbacon.NightOfTheDead;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.bobbacon.item.ModItems;

@Mixin(GlassBottleItem.class)
public class GlassBottleItemMixin extends Item {
    public GlassBottleItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use",at= @At("RETURN"), cancellable = true)
    private void injectUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){

        GlassBottleItem self= (GlassBottleItem)(Object) this;
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        BlockPos blockPos = blockHitResult.getBlockPos();
        ItemStack itemStack = user.getStackInHand(hand);


        if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
            if (world.isClient && NightOfTheDead.isNightOfTheDead){
                cir.setReturnValue(TypedActionResult.success(self.fill(itemStack, user, ModItems.BLOODY_WATER.getDefaultStack())));
            }else if (NightOfTheDead.isNightOfTheDead((ServerWorld) world)){
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                cir.setReturnValue(TypedActionResult.success(self.fill(itemStack, user, ModItems.BLOODY_WATER.getDefaultStack()), world.isClient()));
            }
            return;

        }
    }
}
