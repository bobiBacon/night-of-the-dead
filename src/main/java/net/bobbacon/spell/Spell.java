package net.bobbacon.spell;

import net.bobbacon.NightOfTheDead;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public  class Spell {
    public Spell(SpellType<? extends Spell> type, World world) {
    }

    public boolean casted= false;
    public boolean tryCast(BlockPos pos, World world){
        NightOfTheDead.LOGGER.info("Try casting spell, pos: "+ pos.toString());
        casted= canCast(pos,world);
        NightOfTheDead.LOGGER.info("result: "+ casted);
        return casted;
    }
    public boolean canCast(BlockPos pos, World world){
        return false;
    }
}
