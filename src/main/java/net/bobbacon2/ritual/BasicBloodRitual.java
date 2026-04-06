package net.bobbacon2.ritual;

import net.bobbacon.ritual.Ritual;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicBloodRitual extends Ritual {
    public BasicBloodRitual(BlockPos center, World world) {
        super(center, world);
    }

    public BasicBloodRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }

    @Override
    public Ritual create(World world, NbtCompound nbtCompound) {
        return new BasicBloodRitual(world,nbtCompound);
    }

    @Override
    public boolean hasRitualSite() {
        return false;
    }
}
