package net.bobbacon.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class EternalFireBlock extends FireBlock {

    public EternalFireBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState());
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState();
    }
    @Override
    public void trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random random, int currentAge){

    }
}
