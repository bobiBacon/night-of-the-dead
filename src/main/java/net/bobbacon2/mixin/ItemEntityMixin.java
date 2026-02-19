package net.bobbacon2.mixin;

import net.bobbacon2.item.ModItems;
import net.bobbacon2.item.Molotov;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Inject(at = @At("TAIL"), method = "tick")
    private void molotovTimer(CallbackInfo ci){
        ItemStack stack = this.getStack();
        if (stack.isOf(ModItems.MOLOTOV)&& Molotov.isLit(stack)){
            ModItems.MOLOTOV.inventoryTick(stack,getWorld(),this,0,true);
        }
    }

    @Shadow
    protected void initDataTracker() {

    }

    @Shadow
    public void readCustomDataFromNbt(NbtCompound nbt) {

    }
    @Shadow
    public void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
