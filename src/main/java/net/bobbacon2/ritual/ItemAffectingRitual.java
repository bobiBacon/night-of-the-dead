package net.bobbacon2.ritual;

import net.bobbacon2.entity.block_entity.AltarBE;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemAffectingRitual extends AltarRitual{

    public ItemAffectingRitual(BlockPos center, World world) {
        super(center, world);
    }

    public ItemAffectingRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }

    @Override
    protected void complete() {
        super.complete();
        AltarBE altarBE = getAltarBE();
        altarBE.setStack(applyEffect(altarBE.getStack()));
    }

    @Override
    public boolean canStart() {
        return super.canStart()&&canAffect(getAltarBE().getStack());
    }

    abstract boolean canAffect(ItemStack stack);
    abstract ItemStack applyEffect(ItemStack stack);
}
