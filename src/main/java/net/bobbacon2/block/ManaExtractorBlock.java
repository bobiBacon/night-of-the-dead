package net.bobbacon2.block;

import net.bobbacon2.entity.block_entity.BrewingBarrelBE;
import net.bobbacon2.entity.block_entity.ManaExtractor;
import net.bobbacon2.entity.block_entity.ModBE;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ManaExtractorBlock extends BlockWithEntity {
    protected ManaExtractorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ManaExtractor(pos,state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos=ctx.getBlockPos();
        Map<Direction,BlockPos> map= new HashMap<>();
        map.put(Direction.NORTH,pos.north());
        map.put(Direction.EAST,pos.east());
        map.put(Direction.WEST,pos.west());
        map.put(Direction.SOUTH,pos.south());
        for (var entry:map.entrySet()){
            if (ctx.getWorld().getBlockState(entry.getValue()).getBlock() instanceof FakeFLuidDrainable){
                return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING,entry.getKey());
            }
        }
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING,ctx.getHorizontalPlayerFacing());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1,8,1,15,16,15);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBE.MANA_EXTRACTOR, ManaExtractor::tick);
    }
}
