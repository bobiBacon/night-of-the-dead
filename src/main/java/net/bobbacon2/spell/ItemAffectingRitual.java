package net.bobbacon2.spell;

import net.minecraft.item.ItemStack;

public interface ItemAffectingRitual {
    boolean canAffect(ItemStack stack);
    ItemStack applyEffect(ItemStack stack);
}
