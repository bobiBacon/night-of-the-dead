package net.bobbacon2.ritual;

import net.bobbacon.ritual.Ritual;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VampiresRitual extends AltarRitual{
    public VampiresRitual(BlockPos center, World world) {
        super(center, world);
    }

    public VampiresRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }

    @Override
    public Ritual create(World world, NbtCompound nbtCompound) {
        return new VampiresRitual(world,nbtCompound);
    }

    @Override
    public void definePhases() {

    }

    @Override
    public boolean hasRitualSite() {
        return checkGround(center.down(),7, Blocks.DEEPSLATE_TILES,world)&&RitualUtils.hasEnoughSupportsWithItem(world,center,8, ModItems.BLOOD_BOTTLE,42);
    }


}
