package net.bobbacon2.spell;

import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.ritual.BasicBloodRitual;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimpleBloodRitual extends AltarRitualSpell{
    public SimpleBloodRitual(SpellDef<? extends Spell> type, World world, LivingEntity user, SimpleBloodRitual template) {
        super(type, world, user, template);
    }

    public SimpleBloodRitual() {
        super();
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new SimpleBloodRitual(type,world,user,this);
    }

    @Override
    public ItemAffectingRitual newRitual(BlockPos center) {
        return new BasicBloodRitual(center,world);
    }

    @Override
    public boolean canCast(BlockPos pos) {

        return super.canCast(pos)&& world.getTimeOfDay()>13000;
    }
}
