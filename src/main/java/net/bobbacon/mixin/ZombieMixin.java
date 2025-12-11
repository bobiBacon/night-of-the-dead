
package net.bobbacon.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ZombieEntity.class)
public class ZombieMixin extends HostileEntity {
    protected ZombieMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "createZombieAttributes", cancellable = true)
    private static void BoostHp(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
//        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
//        builder.add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0);
//        cir.setReturnValue(builder);
    }
    @Inject(method = "applyAttributeModifiers", at = @At("TAIL"))
    private void onSpawn(float chanceMultiplier, CallbackInfo ci) {
        this.setHealth(50);
    }


    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float BoostDamage(float amount, DamageSource source){
        //is client check
        float base = amount;
        if (source.isIn(DamageTypeTags.IS_FIRE) || source.isIn(DamageTypeTags.IS_LIGHTNING) || source.isIn(DamageTypeTags.IS_EXPLOSION)){
            amount = amount + base*4;
        }
        if (source.getAttacker()!=null && source.getAttacker() instanceof LivingEntity){
            ItemStack stack = ((LivingEntity) Objects.requireNonNull(source.getAttacker())).getStackInHand(Hand.MAIN_HAND);
            if (stack.hasEnchantments() && EnchantmentHelper.getLevel(Enchantments.SMITE, stack) > 0) {
                amount = amount + base*EnchantmentHelper.getLevel(Enchantments.SMITE, stack)*0.10F;
            }
            if (stack.getItem()  instanceof ToolItem item){
                if (item.getMaterial() == ToolMaterials.GOLD){
                    amount = amount + base*2;
                }

            }
        }


        return amount;
    }
}
