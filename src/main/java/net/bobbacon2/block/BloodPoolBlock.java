package net.bobbacon2.block;

import com.google.common.collect.ImmutableMap;
import net.bobbacon2.entity.block_entity.AltarBE;
import net.bobbacon2.entity.block_entity.BloodPool;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.SimpleVoxelShape;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BloodPoolBlock extends BlockWithEntity {
    protected final static VoxelShape shape1= Block.createCuboidShape(3,0,3,13,1,13);
    protected final static VoxelShape shape2= Block.createCuboidShape(2,1,2,14,2,14);
    protected final static VoxelShape shape3= Block.createCuboidShape(1,2,1,15,6,15);
    protected final static VoxelShape shape= VoxelShapes.union(shape1,shape2,shape3);
    protected BloodPoolBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BloodPool(pos,state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape;
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof BloodPool be){
            return be.onUse(state, world, pos,  player,  hand,  hit);
        }
        return ActionResult.PASS;
    }
}
