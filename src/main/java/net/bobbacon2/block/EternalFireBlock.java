package net.bobbacon2.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class EternalFireBlock extends AbstractFireBlock {

    public EternalFireBlock(Settings settings) {
        super(settings,1.5f);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean bl2 = world.getBiome(pos).isIn(BiomeTags.INCREASED_FIRE_BURNOUT);
        int k = bl2 ? -50 : 0;
        this.trySpreadingFire(world, pos.east(), 300 + k, random);
        this.trySpreadingFire(world, pos.west(), 300 + k, random);
        this.trySpreadingFire(world, pos.down(), 250 + k, random);
        this.trySpreadingFire(world, pos.up(), 250 + k, random);
        this.trySpreadingFire(world, pos.north(), 300 + k, random);
        this.trySpreadingFire(world, pos.south(), 300 + k, random);
    }
    public void trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random random) {
        BlockState blockState = world.getBlockState(pos);

        int i = (int) (((FireBlock)Blocks.FIRE).getSpreadChance(blockState)*2);
        if (random.nextInt(spreadFactor) < i) {

            world.setBlockState(pos, FireBlock.getState(world,pos), Block.NOTIFY_ALL);


            Block block = blockState.getBlock();
            if (block instanceof TntBlock) {
                TntBlock.primeTnt(world, pos);
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getDefaultState();
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }


    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {

    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.isFireImmune()) {
            entity.setOnFireFor(50);

            entity.damage(world.getDamageSources().inFire(), this.damage);
        }


    }

}
