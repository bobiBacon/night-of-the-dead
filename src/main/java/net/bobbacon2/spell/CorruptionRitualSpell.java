package net.bobbacon2.spell;

import net.bobbacon.ritual.Ritual;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.entity.block_entity.AltarBE;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.ritual.CorruptionRitual;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CorruptionRitualSpell extends AltarRitualSpell {


    public CorruptionRitualSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, CorruptionRitualSpell template) {
        super(type, world,user,template);
    }

    public CorruptionRitualSpell(){
        super();
    }

    @Override
    public Spell createFromTemplate(SpellDef<? extends Spell> type, World world, LivingEntity user) {
        return new CorruptionRitualSpell(type,world,user,this);
    }

    @Override
    public ItemAffectingRitual newRitual(BlockPos center) {
        return new CorruptionRitual(center,world);
    }






}
