package net.bobbacon2.mixin;

import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellSchools;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.accessors.PlayerAccessor;
import net.bobbacon2.components.EvolutionApi;
import net.bobbacon2.evolution.ModEvolutions;
import net.bobbacon2.status_effect.ModEffects;
import net.bobbacon2.status_effect.Vampiring;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerMixin  implements PlayerAccessor {
    @Unique
    private boolean isVampire=false;
    @Inject(method = "tick", at= @At("TAIL"))
    private void injectTick(CallbackInfo ci){
        PlayerEntity self= (PlayerEntity) (Object) this;

        if (!self.getWorld().isClient()&&(self.age&15)==0) {
            if (self.isSubmergedInWater()&&NightOfTheDead.isNightOfTheDead((ServerWorld) self.getWorld())){
                self.addStatusEffect(new StatusEffectInstance(ModEffects.INSANITY,200,0));
            }
        }
    }
    @Inject(method = "tickMovement", at= @At("TAIL"))
    private void burnTick(CallbackInfo ci){
        PlayerEntity self= (PlayerEntity) (Object) this;

        if (self.isAlive()&& (EvolutionApi.getEvolution(self).burnsInDaylight()||self.hasStatusEffect(ModEffects.Vampiring))) {
            boolean bl = this.isAffectedByDaylight();
            if (bl) {
                ItemStack itemStack = self.getEquippedStack(EquipmentSlot.HEAD);
                if (!itemStack.isEmpty()) {
                    if (itemStack.isDamageable()) {
                        itemStack.setDamage(itemStack.getDamage() + self.getWorld().random.nextInt(2));
                        if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                            self.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
                            self.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    bl = false;
                }

                if (bl) {
                    self.setOnFireFor(4);
                }
            }
        }

    }
    protected boolean isAffectedByDaylight() {
        PlayerEntity self= (PlayerEntity) (Object) this;

        if (self.getWorld().isDay() && !self.getWorld().isClient) {
            float f = self.getBrightnessAtEyes();
            BlockPos blockPos = BlockPos.ofFloored(self.getX(), self.getEyeY(), self.getZ());
            boolean bl = self.isWet() || self.inPowderSnow || self.wasInPowderSnow;
            if (f > 0.5F && self.getWorld().random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !bl && self.getWorld().isSkyVisible(blockPos)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isVampire() {
        PlayerEntity self= (PlayerEntity) (Object) this;
        return self.hasStatusEffect(ModEffects.Vampiring)||EvolutionApi.getEvolution(self)== ModEvolutions.VAMPIRE;
    }


}
