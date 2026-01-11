package net.bobbacon.spell;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.block_entity.AltarBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CorruptionRitualSpell extends Spell {


    public CorruptionRitualSpell(SpellType<? extends Spell> type, World world) {
        super(type, world);
    }



    @Override
    public boolean canCast(BlockPos pos,World world) {
        BlockEntity be= this.findNearestAltar(pos,world);
        if (be instanceof AltarBE altarBE){

            return altarBE.canCastRitual();
        }
        return false;
    }
    @Nullable
    public BlockEntity findNearestAltar(BlockPos center, World world){


        int rx = 2;
        int ry = 1;
        int rz = 2;

        BlockPos closest = null;
        double closestSq = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.iterate(
                center.add(-rx, -ry, -rz),
                center.add( rx,  ry,  rz)
        )) {
            BlockState state = world.getBlockState(pos);

            if (!state.isOf(ModBlocks.ALTAR)) continue;

            double distSq = pos.getSquaredDistance(center);

            if (distSq < closestSq) {
                closestSq = distSq;
                closest = pos.toImmutable();
            }
        }

        if (closest != null) {
            return world.getBlockEntity(closest);
        }
        return null;
    }

    @Override
    public boolean tryCast(BlockPos pos, World world) {
        boolean b = super.tryCast(pos, world);
        if (b){
            BlockEntity be = findNearestAltar(pos,world);
            if (be instanceof AltarBE altarBE){
                altarBE.tryCastRitual();
            }
        }
        return b;
    }

    @Override
    public boolean isSingleUse() {
        return true;
    }
}
