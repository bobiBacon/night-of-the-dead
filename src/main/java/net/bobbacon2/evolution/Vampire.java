package net.bobbacon2.evolution;

import net.bobbacon.components.FloatWithModifiers;
import net.bobbacon.components.SpellsStatApi;
import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellSchools;
import net.bobbacon2.utils.EntityUtils;
import net.bobbacon2.utils.TimeUtils;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class Vampire extends Evolution{
    private static UUID VampireMana= UUID.fromString("a8af138b-7520-4ed9-b677-1a8a0f7b6cad");
    private static final UUID BONUS_HEALTH_UUID = UUID.fromString("81795248-c755-45f8-80f0-e859869c8e9e");
    private static final UUID SPELL_POWER_ID = UUID.fromString("77386235-d94a-43c3-a334-16ec4da61291");
    @Override
    public void applyAttributeModifiers(PlayerEntity player) {
        Mana.getSchoolsMana(player).get(SpellSchools.Necromancy).addMaxModifier(VampireMana,40, FloatWithModifiers.OperationType.ADDITION);
        SpellsStatApi.addModifier(SpellsStatApi.getSpellPowerComponent(player),SpellSchools.Necromancy,SPELL_POWER_ID,1.4f, FloatWithModifiers.OperationType.MULTIPLICATION);
        SpellsStatApi.addModifier(SpellsStatApi.getCastSpeedComponent(player),SpellSchools.Necromancy,SPELL_POWER_ID,0.8f, FloatWithModifiers.OperationType.MULTIPLICATION);
        SpellsStatApi.addModifier(SpellsStatApi.getManaRegenComponent(player),SpellSchools.Necromancy,SPELL_POWER_ID,1.3f, FloatWithModifiers.OperationType.MULTIPLICATION);
        EntityUtils.addAdditionModifier(player, EntityAttributes.GENERIC_MAX_HEALTH,BONUS_HEALTH_UUID, "vampire_boost",6);
        player.heal(6);
    }



    @Override
    public void removeAttributeModifiers(PlayerEntity player) {
        Mana.getSchoolsMana(player).get(SpellSchools.Necromancy).removeMaxModifier(VampireMana);
        SpellsStatApi.removeModifier(SpellsStatApi.getSpellPowerComponent(player),SpellSchools.Necromancy,SPELL_POWER_ID);
        SpellsStatApi.removeModifier(SpellsStatApi.getCastSpeedComponent(player),SpellSchools.Necromancy,SPELL_POWER_ID);
        SpellsStatApi.removeModifier(SpellsStatApi.getManaRegenComponent(player),SpellSchools.Necromancy,SPELL_POWER_ID);
        EntityUtils.removeModifier(player,EntityAttributes.GENERIC_MAX_HEALTH,BONUS_HEALTH_UUID);
        player.setHealth(Math.min(player.getMaxHealth(),player.getHealth()));

    }

    @Override
    public boolean burnsInDaylight() {
        return true;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public void tick(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,222,0,false,false));
    }
}
