package net.bobbacon2.spell;

import net.bobbacon.ritual.Ritual;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.entity.block_entity.AltarBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AltarRitualSpell extends Spell {
    public AltarRitualSpell(SpellDef<? extends Spell> type, World world, LivingEntity user, AltarRitualSpell template) {
        super(type, world, user, template);
    }

    public AltarRitualSpell() {
    }

    @Override
    public boolean canCast(BlockPos pos) {
        if (!super.canCast(pos)){
            return false;
        }
        BlockEntity be= this.findNearestAltar(pos,world);
        if (be instanceof AltarBE altarBE){

            return altarBE.canCastRitual((Ritual) newRitual(be.getPos()));
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
    public boolean tryCast(BlockPos pos) {
        boolean b = super.tryCast(pos);
        if (b){
            BlockEntity be = findNearestAltar(pos,world);
            if (be instanceof AltarBE altarBE){
                altarBE.tryCastRitual(newRitual(altarBE.getPos()));
            }
        }
        return b;
    }
    public abstract ItemAffectingRitual newRitual(BlockPos center);
}
