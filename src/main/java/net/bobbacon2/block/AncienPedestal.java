package net.bobbacon2.block;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.item.ModItems;
import net.bobbacon2.sound.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AncienPedestal extends ChargeableBlock implements DeathReceptorBlock{
    public AncienPedestal(Settings settings) {
        super(settings,5);
    }
    @Override
    public void onEntityDiedOn(World world, BlockPos pos, BlockState state,LivingEntity entity, DamageSource source) {
        Entity attacker = source.getAttacker();
        if (entity instanceof VillagerEntity&& attacker instanceof PlayerEntity player&& player.getMainHandStack().isOf(ModItems.SACRIFICIAL_DAGGER)){
            charge(world,pos,state,1);
            if (isFullyCharged(world.getBlockState(pos))){
                world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ANCIENT_PEDESTAL_FINAL_CHARGE, SoundCategory.NEUTRAL, 5.0F, 1F);
            }else {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ANCIENT_PEDESTAL_CHARGE, SoundCategory.NEUTRAL, 5.0F, 1F);
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0,0,0,16,8,16);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
    }
}
