package net.bobbacon.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onSpawnPacket", at = @At("TAIL"))
    private void onSpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof AbstractSkeletonEntity){
            self.setHealth(40);
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float BoostDamage(float amount, DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof AbstractSkeletonEntity){
            float base = amount;
            ItemStack stack = ((LivingEntity) Objects.requireNonNull(source.getAttacker())).getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem()  instanceof ToolItem item){
                if (item.getMaterial() == ToolMaterials.GOLD){
                    amount = amount + base;
                }
                if (stack.getItem() instanceof AxeItem || stack.getItem() instanceof ShovelItem){
                    amount = amount + base;
                }
            }
            if (source.isIn(DamageTypeTags.IS_LIGHTNING) || source.isIn(DamageTypeTags.IS_EXPLOSION)){
                amount = amount + base*4;
            }
            if (source.isIn(DamageTypeTags.IS_FIRE)){
                amount = amount + base;
            }
        }
        return amount;
    }

        @Override
    @Shadow
    protected void initDataTracker() {

    }

    @Override
    @Shadow
    public void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    @Shadow
    public void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
