package net.bobbacon.screen;

import net.bobbacon.entity.block_entity.Refinery;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;

public class RefiningInventory extends CraftingInventory implements RecipeInputInventory {
    Refinery refinery;
    public RefiningInventory(Refinery refinery, int width, int height) {
        super(refinery.screenHandler, width, height);
        this.refinery=refinery;
    }

    public RefiningInventory(Refinery refinery, int width, int height, DefaultedList<ItemStack> stacks) {
        super(refinery.screenHandler, width, height, stacks);
        this.refinery=refinery;

    }
    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);

        if (!itemStack.isEmpty()) {
            this.refinery.onContentChanged();
        }
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
            this.refinery.onContentChanged();

    }
}
