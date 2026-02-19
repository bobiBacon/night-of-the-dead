package net.bobbacon2.screen;

import net.bobbacon2.entity.block_entity.Refinery;
import net.bobbacon2.recipe.ModRecipes;
import net.bobbacon2.recipe.RefiningRecipe;
import net.bobbacon2.screen.slot.RefineryFuelSlot;
import net.bobbacon2.screen.slot.RefineryOutputSlot;
import net.bobbacon2.entity.block_entity.Refinery;
import net.bobbacon2.screen.slot.RefineryFuelSlot;
import net.bobbacon2.screen.slot.RefineryOutputSlot;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RefiningScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final Refinery blockEntity;
    public RecipeInputInventory craftingInventory ;
    private final World world;
    private final PlayerEntity player;
    private final PlayerInventory playerInventory;
    private final ScreenHandlerContext context;


    public RefiningScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(4));
    }

    public RefiningScreenHandler(int syncId, PlayerInventory playerInventory,
                                 BlockEntity blockEntity, PropertyDelegate PropertyDelegate) {
        super(ModScreenHandlers.REFINERY_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 3);

        this.inventory= (Inventory) blockEntity;
        inventory.onOpen(playerInventory.player);
        this.playerInventory= playerInventory;
        this.propertyDelegate = PropertyDelegate;
        this.blockEntity = ((Refinery) blockEntity);
        craftingInventory= ((Refinery) blockEntity).craftingInventory;
        this.context=ScreenHandlerContext.create(playerInventory.player.getWorld(), blockEntity.getPos());
        this.world = playerInventory.player.getWorld();
        this.player= playerInventory.player;

        this.addSlot(new RefineryFuelSlot(inventory,0,11,21));
        this.addSlot(new Slot(inventory,1,100,53));
        this.addSlot(new RefineryOutputSlot(player,inventory,2,129,35));
        addCraftingSlots();

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(PropertyDelegate);
    }


    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }
    public boolean isBurning() {
        return propertyDelegate.get(2) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 22; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    public int getFuelValue() {
        int fuel = this.propertyDelegate.get(2);
        int maxFuel = this.propertyDelegate.get(3);  // Max Progress
        int holderSize = 39; // This is the width in pixels of your arrow

        return maxFuel != 0 && fuel != 0 ? fuel * holderSize / maxFuel : 0;
    }
    public boolean tryGiveRemaningStack(ItemStack itemStack){
        return this.playerInventory.insertStack(itemStack);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            itemStack = originalStack.copy();
            if (slotIndex<12){
                if (!this.insertItem(originalStack, 12, this.slots.size(), false)){
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (!this.insertItem(originalStack,0,12,false)){
                    if (slotIndex>39){
                        if (!this.insertItem(originalStack,12,39,false)){
                            return ItemStack.EMPTY;
                        }
                    }else {
                        if (!this.insertItem(originalStack,40,slots.size(),false)){
                            return ItemStack.EMPTY;
                        }
                    }
                }
                //comportement vanilla

            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;

    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 83 + i * 18));
            }
        }
    }
    private void addCraftingSlots(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.craftingInventory, j + i * 3, 38 + j * 18, 17 + i * 18));
            }
        }
    }
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 141));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        blockEntity.onContentChanged();
    }
}
