package net.bobbacon2.spell;

import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon2.ritual.AltarRitual;
import net.bobbacon2.ritual.ItemAffectingRitual;
import net.bobbacon2.ritual.VampiresRitual;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VampiresRitualSpell extends AltarRitualSpell {


    public VampiresRitualSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, VampiresRitualSpell template) {
        super(type, world,user,template);
    }

    public VampiresRitualSpell(){
        super();
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new VampiresRitualSpell(type,world,user,this);
    }

    @Override
    public AltarRitual newRitual(BlockPos center) {
        return new VampiresRitual(center,world);
    }






}
