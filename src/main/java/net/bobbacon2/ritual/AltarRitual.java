package net.bobbacon2.ritual;

import net.bobbacon.ritual.Ritual;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.entity.block_entity.AltarBE;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AltarRitual extends Ritual {
    public AltarRitual(BlockPos center, World world) {
        super(center, world);
    }

    public AltarRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }

    @Override
    public boolean canStart() {
        return super.canStart()&&getAltar().isOf(ModBlocks.ALTAR);
    }

    @Override
    public void abort() {
        getAltarBE().removeRitual();
        super.abort();
    }

    @Override
    protected void complete() {
        super.complete();
        NightOfTheDead.LOGGER.info("Completed! You're a Vampire!");
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
