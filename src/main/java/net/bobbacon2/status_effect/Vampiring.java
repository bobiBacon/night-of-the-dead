package net.bobbacon2.status_effect;

import net.bobbacon.components.ManaComponent;
import net.bobbacon.components.ModComponents;
import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellSchool;
import net.bobbacon.spell.SpellSchools;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.accessors.PlayerAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class Vampiring extends StatusEffect {
    UUID id= UUID.fromString("9c6d7a32-465f-4653-86ea-9bbaf520abf7");
    protected Vampiring(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player){
            Map<SpellSchool, Mana> manaMap = Mana.getSchoolsMana(player);
            manaMap.get(SpellSchools.Necromancy).addMaxModifier(id,20);
        }

    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player){
            Map<SpellSchool, Mana> manaMap = Mana.getSchoolsMana(player);
            manaMap.get(SpellSchools.Necromancy).removeMaxModifier(id);
        }
    }
}
