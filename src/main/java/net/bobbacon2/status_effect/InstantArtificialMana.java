package net.bobbacon2.status_effect;

import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellSchools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class InstantArtificialMana extends InstantStatusEffect {

    public InstantArtificialMana(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        super.applyInstantEffect(source, attacker, target, amplifier, proximity);
        if (target instanceof PlayerEntity player){
            Mana mana = Mana.getSchoolsMana(player).get(SpellSchools.Necromancy);
            mana.addMana(20);
        }
    }
}
