package net.bobbacon2.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bobbacon.Accessors.EntityAccessor;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.accessors.PlayerAccessor;
import net.bobbacon2.block.AncienPedestal;
import net.bobbacon2.block.DeathReceptorBlock;
import net.bobbacon2.damage.ModDamageTypes;
import net.bobbacon2.enchants.ModEnchantments;
import net.bobbacon2.status_effect.ModEffects;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends Entity {
    @Shadow private @Nullable LivingEntity attacker;
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
        if (self.getGroup()==EntityGroup.UNDEAD){
            if (source.isOf(ModDamageTypes.Holy)){
                return amount*7;
            }
        }
        if (((EntityAccessor)(this)).the_spell_library$comesFromRitual()&&source.isIn(DamageTypeTags.IS_EXPLOSION)){
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
    @ModifyVariable(
            method = "applyDamage",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/LivingEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
            ),
            ordinal = 0
    )
    private float modifyDamageAfterArmor(float amount, DamageSource source) {
        Entity entity =source.getAttacker();
        if (entity instanceof LivingEntity livingEntity&& EnchantmentHelper.getLevel(ModEnchantments.Vampire,livingEntity.getMainHandStack())>0){
            LivingEntity self= (LivingEntity)(Object)this;
            for (int i = 0; i < 20; i++) {
                World world = livingEntity.getWorld();
                Vec3d targetPos= new Vec3d(self.getParticleX(0.7),self.getRandomBodyY(),self.getParticleZ(0.7));
                Random random = world.getRandom();
                Vec3d toAttacker= livingEntity.getEyePos().add(random.nextFloat()*0.4f-0.2f,random.nextFloat()*0.4f-0.2f,random.nextFloat()*0.4f-0.2f).subtract(targetPos).normalize();
                ((ServerWorld) world).spawnParticles(new DustParticleEffect(Vec3d.unpackRgb(0x880808).toVector3f(),1),targetPos.getX() ,targetPos.getY(),targetPos.getZ(),1,toAttacker.getX(),toAttacker.getY(),toAttacker.getZ(),0);
            }
            float multiplicator= 0.15f;
            if (livingEntity instanceof PlayerEntity player&&((PlayerAccessor)player).isVampire()){
                multiplicator=0.35f;
            }
            livingEntity.heal(amount*multiplicator);
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
    @ModifyReturnValue(method = "getGroup",at=@At("RETURN"))
    private EntityGroup makeUndead(EntityGroup original){
        LivingEntity self= (LivingEntity) (Object) this;
        if (self instanceof PlayerEntity player){
            return ((PlayerAccessor)player).isVampire()? EntityGroup.UNDEAD:EntityGroup.DEFAULT;
        }
        return original;
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
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;


        BlockPos pos = entity.getLandingPos();
        BlockState state = entity.getWorld().getBlockState(pos);

        if (state.getBlock() instanceof DeathReceptorBlock block) {
            block.onEntityDiedOn(entity.getWorld(), pos,state, entity, source);
        }
    }


}
