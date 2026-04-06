package net.bobbacon2.status_effect;

import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellSchools;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class ArtificialManaBoost extends StatusEffect {
    UUID modifier_id= UUID.fromString("1ffabd47-9401-4af8-ba08-1ecca0b4c0e0");
    protected ArtificialManaBoost(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player){
            Mana mana = Mana.getSchoolsMana(player).get(SpellSchools.Necromancy);
            mana.addMaxModifier(modifier_id,20);
            mana.addMana(20);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player){
            Mana mana = Mana.getSchoolsMana(player).get(SpellSchools.Necromancy);
            mana.removeMaxModifier(modifier_id);
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,200,1));
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration<=5;
    }
}
