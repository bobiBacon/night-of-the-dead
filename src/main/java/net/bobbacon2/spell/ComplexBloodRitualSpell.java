package net.bobbacon2.spell;

import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.ritual.ComplexBloodRitual;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ComplexBloodRitualSpell extends AltarRitualSpell{
    public ComplexBloodRitualSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, ComplexBloodRitualSpell template) {
        super(type, world, user, template);
    }

    public ComplexBloodRitualSpell() {
        super();
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new ComplexBloodRitualSpell(type,world,user,this);
    }

    @Override
    public ItemAffectingRitual newRitual(BlockPos center) {
        return new ComplexBloodRitual(center,world);
    }
    @Override
    public boolean canCast(BlockPos pos) {
        return super.canCast(pos)&& world.getTimeOfDay()>13000;
    }
}
