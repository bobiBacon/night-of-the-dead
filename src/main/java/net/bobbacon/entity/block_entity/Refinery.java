package net.bobbacon.entity.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class Refinery extends BlockEntity {
    public Refinery(BlockPos pos, BlockState state) {
        super(ModBE.REFINERY, pos, state);
    }


}
