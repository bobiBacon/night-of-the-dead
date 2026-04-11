package net.bobbacon2.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface DeathReceptorBlock {
    public void onEntityDiedOn(World world, BlockPos pos, BlockState state, LivingEntity entity, DamageSource source);
}
