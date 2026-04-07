package net.bobbacon2.ritual;

import net.bobbacon.ritual.Ritual;
import net.bobbacon2.entity.block_entity.AltarBE;
import net.bobbacon2.spell.ItemAffectingRitual;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AltarRitual extends Ritual implements ItemAffectingRitual {
    public AltarRitual(BlockPos center, World world) {
        super(center, world);
    }

    public AltarRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }

    @Override
    protected void complete() {
        super.complete();
        AltarBE altarBE = getAltarBE();
        altarBE.setStack(applyEffect(altarBE.getStack()));
    }
    @Override
    public void abort() {
        getAltarBE().removeRitual();
        super.abort();
    }
    public AltarBE getAltarBE(){
        if (world.getBlockEntity(center) instanceof AltarBE be){
            return be;
        }
        return null;
    }
    public BlockState getAltar(){
        return world.getBlockState(center);
    }
}
