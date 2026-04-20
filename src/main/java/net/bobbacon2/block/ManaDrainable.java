package net.bobbacon2.block;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ManaDrainable {
    public boolean canDrain(BlockPos pos, World world);
    public FakeFluidInstance drain(BlockPos pos, World world);
    public FakeFluid getFluidType(BlockPos pos, World world);
}
