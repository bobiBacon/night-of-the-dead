package net.bobbacon.status_effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class Insanity extends StatusEffect {
    protected Insanity(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = Math.max(50 >> amplifier,25);
        return duration % i == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,80,0,false,false));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,80,amplifier+1,false,false));
        if (amplifier>0){
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,80,amplifier,false,false));
        }
        if (amplifier>1){
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,140,0,false,false));
        }
        if (entity.getWorld().isClient()&&entity instanceof PlayerEntity player){
            World world= entity.getWorld();
            Random random = new Random();
            world.playSound(player,entity.getX(),entity.getY(),entity.getZ(), SoundEvents.ENTITY_WITCH_AMBIENT, SoundCategory.HOSTILE,0.7f*amplifier+0.8f*random.nextFloat(),0.7f+random.nextFloat()*0.6f, random.nextLong());
        }


    }
}
