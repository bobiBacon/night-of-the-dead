package net.bobbacon2.block;

import net.bobbacon2.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class SoulWormTumor extends ChargeableBlock implements FakeFLuidDrainable {

    public SoulWormTumor(Settings settings) {
        super(settings,110);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stackInHand = player.getStackInHand(hand);
        if (stackInHand.isOf(ModItems.SOUL_BOTTLE)&&!isFullyCharged(state)){
            stackInHand.decrement(1);
            player.setStackInHand(hand,stackInHand);
            player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
            charge(world,pos,state,1);
            if (!world.isClient()){
                ((ServerWorld)world).spawnParticles(ParticleTypes.SQUID_INK,pos.getX()+0.5f,pos.getY()+0.5f,pos.getZ()+0.5f,8,0.8,0.8,0.8,0);

            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1,0,1,15,16,15);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }


    @Override
    public boolean canDrain(BlockPos pos, World world) {
        return getCharge(world.getBlockState(pos))>=50;

    }

    @Override
    public FakeFluidInstance drain(BlockPos pos, World world) {
        consume(world,pos,world.getBlockState(pos),40);
        return new FakeFluidInstance(getFluidType(pos,world),3);
    }

    @Override
    public FakeFluidInstance getFluidThatCanBeDrained(BlockPos pos, World world) {
        return canDrain(pos,world)?new FakeFluidInstance(getFluidType(pos,world),3):new FakeFluidInstance(FakeFluids.EMPTY,0);
    }

    @Override
    public FakeFluid getFluidType(BlockPos pos, World world) {
        return FakeFluids.CRUSHED_SOULS;
    }
    public static void generate(ServerWorld world){
        for (PlayerEntity player : world.getPlayers()) {

            BlockPos base = player.getBlockPos();

            BlockPos pos = base.add(
                    world.random.nextBetween(-200, 200),
                    world.random.nextBetween(-100, 100),
                    world.random.nextBetween(-200, 200)
            );

            pos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);

            if (world.isAir(pos) && world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                world.setBlockState(pos, ModBlocks.SOUL_WORM_TUMOR.getDefaultState());
            }
        }
    }
}
