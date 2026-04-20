package net.bobbacon2.block;

import net.bobbacon2.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ChargeableBlock extends Block {
    public final int maxCharge;
    public static final IntProperty CHARGE= IntProperty.of("charge", 0, 2000);
    public ChargeableBlock(Settings settings, int maxCharge) {
        super(settings);
        this.maxCharge = maxCharge;
        if (maxCharge>=2000)throw new IllegalArgumentException();
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(CHARGE);
    }
    public boolean isFullyCharged(BlockState state){
        return state.get(CHARGE)>=maxCharge;
    }
    public int getCharge(BlockState state){
        return state.get(CHARGE);
    }
    public void charge(World world,BlockPos pos,BlockState state, int amount){
        Integer i = state.get(CHARGE);
        int newCharge = Math.min(i + amount, maxCharge);
        if (!world.isClient()){
            world.setBlockState(pos,state.with(CHARGE, newCharge));
        }

    }
    public void consumeAll(World world, BlockPos pos, BlockState state){
        world.setBlockState(pos,state.with(CHARGE, 0));
    }
    public void consume(World world, BlockPos pos, BlockState state, int amount){
        world.setBlockState(pos,state.with(CHARGE, state.get(CHARGE)-amount));
    }


}
