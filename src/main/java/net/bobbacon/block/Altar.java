package net.bobbacon.block;

import com.mojang.serialization.MapCodec;
import net.bobbacon.entity.block_entity.AltarBE;
import net.bobbacon.entity.block_entity.BrewingBarrelBE;
import net.bobbacon.ritual.RitualManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Altar extends BlockWithEntity {
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    private static final VoxelShape Z_STEP_SHAPE = Block.createCuboidShape(3.0, 4.0, 4.0, 13.0, 5.0, 12.0);
    private static final VoxelShape Z_STEM_SHAPE = Block.createCuboidShape(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);
    private static final VoxelShape Z_FACE_SHAPE = Block.createCuboidShape(0.0, 10.0, 3.0, 16.0, 16.0, 13.0);
    private static final VoxelShape X_STEP_SHAPE = Block.createCuboidShape(4.0, 4.0, 3.0, 12.0, 5.0, 13.0);
    private static final VoxelShape X_STEM_SHAPE = Block.createCuboidShape(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);
    private static final VoxelShape X_FACE_SHAPE = Block.createCuboidShape(3.0, 10.0, 0.0, 13.0, 16.0, 16.0);
    private static final VoxelShape Z_AXIS_SHAPE = VoxelShapes.union(BASE_SHAPE, Z_STEP_SHAPE, Z_STEM_SHAPE, Z_FACE_SHAPE);
    private static final VoxelShape X_AXIS_SHAPE = VoxelShapes.union(BASE_SHAPE, X_STEP_SHAPE, X_STEM_SHAPE, X_FACE_SHAPE);
    protected Altar(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBE(pos, state);
    }



    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())){
            if (!world.isClient()){

                if (world.getBlockEntity(pos) instanceof AltarBE altar){
                    RitualManager.get((ServerWorld) world).remove(altar.ritualId);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof AltarBE be){
            return be.onUse(state, world, pos,  player,  hand,  hit);
        }
        return ActionResult.PASS;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(Properties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? X_AXIS_SHAPE : Z_AXIS_SHAPE;

    }

}
