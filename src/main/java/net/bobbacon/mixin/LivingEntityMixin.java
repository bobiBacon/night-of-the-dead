package net.bobbacon.mixin;

import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon.status_effect.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends Entity implements LivingEntityAccessor {
    @Shadow private @Nullable LivingEntity attacker;
    @Unique
    public boolean comesFromRitual= false;
    @Unique
    private static final String comesFromRitualKey= "comes_from_ritual";
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
        if (comesFromRitual&&source.isIn(DamageTypeTags.IS_EXPLOSION)){
            return 0;
        }
        if (self instanceof AbstractSkeletonEntity){
            float base = amount;
            if (source.getAttacker() instanceof LivingEntity attacker){
                ItemStack stack = attacker.getStackInHand(Hand.MAIN_HAND);
                if (stack.getItem()  instanceof ToolItem item){
                    if (item.getMaterial() == ToolMaterials.GOLD){
                        amount = amount + base;
                    }
                    if (stack.getItem() instanceof AxeItem || stack.getItem() instanceof ShovelItem){
                        amount = amount + base/3;
                    }
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

    @ModifyVariable(method = "heal", at = @At("HEAD"), argsOnly = true)
    private float modifyHeal(float value){
        LivingEntity self= (LivingEntity) (Object) this;
        StatusEffectInstance effect= self.getStatusEffect(ModEffects.ATTRITION);
        if (effect!=null) {
            int i= self.getGroup()== EntityGroup.UNDEAD? -1:1;
            value -= 0.25f*effect.getAmplifier()*value*i;
        }
        return value;
    }
    @Inject(method = "onDeath",at = @At("TAIL"))
    private void onDeathInject(DamageSource damageSource, CallbackInfo ci){
        if (!getWorld().isClient){
            RitualManager.get((ServerWorld) getWorld()).onEntityDeath((LivingEntity) (Object) this);
        }
    }
//    @ModifyReturnValue(method = "isFire")
//    private boolean immuneToFire(){
//
//    }
    @Inject(method = "writeCustomDataToNbt", at=@At("TAIL"))
    private void writeNbt(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean(comesFromRitualKey,comesFromRitual);
    }
    @Inject(method = "readCustomDataFromNbt", at=@At("TAIL"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci){
        comesFromRitual=nbt.getBoolean(comesFromRitualKey);
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

    @Override
    public boolean night_of_the_Dead$comesFromRitual() {
        return comesFromRitual;
    }

    @Override
    public void night_of_the_Dead$setComesFromRitual(boolean value) {
        comesFromRitual= value;
    }
}
