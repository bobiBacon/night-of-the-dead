package net.bobbacon2.entity.block_entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ManaExtractor extends BlockEntity {
    public ManaExtractor(BlockPos pos, BlockState state) {
        super(ModBE.MANA_EXTRACTOR, pos, state);
    }
}
