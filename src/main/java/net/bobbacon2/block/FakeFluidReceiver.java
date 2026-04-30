package net.bobbacon2.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface FakeFluidReceiver {
    public void receiveFluid(FakeFluidInstance fakeFluidInstance, World world, BlockPos pos);
    public boolean canReceive(FakeFluidInstance fakeFluidInstance, World world, BlockPos pos);
    public FakeFluidInstance getFluid(World world, BlockPos pos);
}
