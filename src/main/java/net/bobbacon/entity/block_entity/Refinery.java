package net.bobbacon.entity.block_entity;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.recipe.ModRecipes;
import net.bobbacon.recipe.RefiningRecipe;
import net.bobbacon.screen.RefiningInventory;
import net.bobbacon.screen.RefiningScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Refinery extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory{
    private static final int PROGRESS_KEY=0;
    private static final int MAX_PROGRESS_KEY=1;
    private static final int FUEL_KEY=2;
    private static final int MAX_FUEL_KEY=3;
    protected final PropertyDelegate propertyDelegate;
    private int maxProgress = 40;
    private int progress= 0;
    int fuelTime;
    int maxFuelTime;
    public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3,ItemStack.EMPTY);
    public final RefiningInventory craftingInventory= new RefiningInventory(this,3,3);
    public RefiningScreenHandler screenHandler;
    private boolean isCrafting= false;
    private final RecipeManager.MatchGetter<RecipeInputInventory, RefiningRecipe> matchGetter;
    public RefiningRecipe currentRecipe=null;




    public Refinery(BlockPos pos, BlockState state) {
        super(ModBE.REFINERY, pos, state);
        matchGetter=RecipeManager.createCachedMatchGetter(ModRecipes.refiningRecipe);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case PROGRESS_KEY -> Refinery.this.progress;
                    case MAX_PROGRESS_KEY -> Refinery.this.maxProgress;
                    case FUEL_KEY -> Refinery.this.fuelTime;
                    case MAX_FUEL_KEY -> Refinery.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case PROGRESS_KEY -> Refinery.this.progress = value;
                    case MAX_PROGRESS_KEY -> Refinery.this.maxProgress = value;
                    case FUEL_KEY -> Refinery.this.fuelTime = value;
                    case MAX_FUEL_KEY -> Refinery.this.maxFuelTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        DefaultedList<ItemStack> items= DefaultedList.ofSize(12,ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) {
            items.set(i,inventory.get(i));
        }
        for (int i = 0; i < 9; i++) {
            items.set(i+3,craftingInventory.getStack(i));
        }

        Inventories.writeNbt(nbt, items);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        DefaultedList<ItemStack> items= DefaultedList.ofSize(12,ItemStack.EMPTY);
        Inventories.readNbt(nbt, items);
        for (int i = 0; i < 3; i++) {
            inventory.set(i,items.get(i));
        }
        for (int i = 0; i < 9; i++) {
            craftingInventory.setStack(i,items.get(i+3));
        }
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.night-of-the-dead.refinery");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        screenHandler= new RefiningScreenHandler(syncId,playerInventory,this,this.propertyDelegate);
        return screenHandler;
    }
    public static void tick(World world, BlockPos pos, BlockState state, Refinery blockEntity) {
        if (world.isClient){
            return;
        }
        if (blockEntity.isCurrentlyCrafting()&& blockEntity.canBurn()){
            blockEntity.increaseProgress();
            if (blockEntity.hasCraftingFinished()){
                blockEntity.craft();
            }
        }else {
            blockEntity.decreaseProgress();
        }
        blockEntity.burn();
        blockEntity.markDirty();

    }

    private void burn() {
        if (fuelTime<=0&&isCurrentlyCrafting()){
            ItemStack stack= inventory.get(0);
            if (stack.isOf(Items.BLAZE_POWDER)&&!stack.isEmpty()){
                stack.decrement(1);
                fuelTime=100;
                maxFuelTime=100;
            }
            return;
        }
        fuelTime--;
    }

    private void decreaseProgress() {
        if (progress>0){
            progress--;
        }
    }

    private void craft() {
        resetProgress();
    }

    private boolean hasCraftingFinished() {
        return progress>=maxProgress;
    }

    private void increaseProgress() {
        progress++;
    }

    public void resetProgress() {
        progress=0;
        isCrafting=false;
    }

    public void startCrafting(RefiningRecipe recipe) {
        progress=0;
        isCrafting=true;
        currentRecipe= recipe;
    }

    public boolean isCurrentlyCrafting() {
        return isCrafting;
    }
    public boolean canBurn(){
        return fuelTime>0||!inventory.get(0).isEmpty();
    }
    public boolean canCraft(RefiningRecipe recipe){
        ItemStack output= inventory.get(2);
        ItemStack recipeStack= recipe.getOutput(world.getRegistryManager());
        return canBurn()&& (ItemStack.canCombine(inventory.get(2),recipeStack) || output.isEmpty()) && output.getCount() + recipeStack.getCount() <= output.getMaxCount() && !isCurrentlyCrafting();
    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity serverPlayerEntity) {

    }
    public Optional<RefiningRecipe> getRecipe() {
        var recipes = world.getRecipeManager().listAllOfType(ModRecipes.refiningRecipe);
        NightOfTheDead.LOGGER.info("Recettes trouvées: " + recipes.size());
        return this.world.getRecipeManager().getFirstMatch(ModRecipes.refiningRecipe,craftingInventory,world);
    }

    @Override
    public void onContentChanged() {
        if (world==null){
            return;
        }
        Optional<RefiningRecipe> optional= getRecipe();
        if (optional.isEmpty()){
            resetProgress();
            return;
        }
        if (!optional.get().equals(currentRecipe)){
            resetProgress();
            if (canCraft(optional.get())){
                startCrafting(optional.get());
            }
        }
    }
}
