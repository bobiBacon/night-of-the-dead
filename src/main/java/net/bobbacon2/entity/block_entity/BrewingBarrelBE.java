package net.bobbacon2.entity.block_entity;

import net.bobbacon2.item.ModItems;
import net.bobbacon2.recipe.AlcoholBrewingRecipe;
import net.bobbacon2.recipe.ModRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class BrewingBarrelBE extends BlockEntity{
    private DefaultedList<ItemStack> items= DefaultedList.ofSize(1,ItemStack.EMPTY);
    private final static String HAS_WATER_KEY = "has_water";
    private final static String NETHER_WARTS_AMOUNT_KEY = "nether_warts_amount";
    private final static String TIME_KEY = "nether_warts_amount";
    private final static String IS_EXPIRED_KEY = "is_expired";
    private static final String PRODUCT_AMOUNT_KEY = "product_amount";

    private final int brewingTime = 36;
    private final int expirationTime = 48000;
    protected int time= 0;
    protected int netherWartsAmount= 0;
    protected boolean hasWater=false;
    protected int productAmount = 0;
    protected boolean isExpired = false;

//    private final RecipeManager.MatchGetter<Inventory, AlcoholBrewingRecipe> matchGetter = RecipeManager.createCachedMatchGetter(ModRecipes.alcoholRecipe);



    public BrewingBarrelBE( BlockPos pos, BlockState state) {
        super(ModBE.BREWING_BARREL_BE, pos, state);

    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt,items);
        time = nbt.getInt(TIME_KEY);
        netherWartsAmount= nbt.getInt(NETHER_WARTS_AMOUNT_KEY);
        productAmount = nbt.getInt(PRODUCT_AMOUNT_KEY);
        isExpired= nbt.getBoolean(IS_EXPIRED_KEY);
        hasWater= nbt.getBoolean(HAS_WATER_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items, true);
        nbt.putInt(TIME_KEY,time);
        nbt.putInt(NETHER_WARTS_AMOUNT_KEY,netherWartsAmount);
        nbt.putInt(PRODUCT_AMOUNT_KEY, productAmount);
        nbt.putBoolean(HAS_WATER_KEY,hasWater);
        nbt.putBoolean(IS_EXPIRED_KEY, isExpired);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, BrewingBarrelBE be) {
        if (world.isClient){
            return;
        }
        if (!be.isBrewing()){
            be.time = 0;
            be.markDirty();

            return;
        }
        if (be.isExpired){
            return;
        }
        if (be.time >= be.expirationTime){
            be.isExpired= true;
        }
        be.time++;
        be.markDirty();
    }

    protected boolean hasEnoughWarts(){
        return this.netherWartsAmount>=5;
    }
    protected boolean hasEnoughWater(){
        return hasWater;
    }
    //not to be called on client
    protected boolean isBrewing(){
        return hasEnoughWater() && hasEnoughWarts() && getRecipeFor(items.get(0)).isPresent()&&items.get(0).getCount()>=64;
    }
    protected void reset(){
        time=0;
        netherWartsAmount= 0;
        productAmount=0;
        isExpired=false;
        items.set(0,ItemStack.EMPTY);
        hasWater=false;
        markDirty();
    }
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        ItemStack stack = player.getStackInHand(hand);
        if (isBrewing()){
            if (stack.isOf(Items.GLASS_BOTTLE)){
                player.giveItemStack(getProduct());
                stack.decrement(1);
                if (productAmount<=0){
                    reset();
                }
                return ActionResult.SUCCESS;
            }
        } else {
            if (stack.isOf(Items.WATER_BUCKET)&&!hasEnoughWater()){
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                hasWater=true;
                markDirty();
                return ActionResult.SUCCESS;

            } else if (stack.isOf(Items.NETHER_WART)&&!hasEnoughWarts()) {
                stack.decrement(1);
                netherWartsAmount++;
                markDirty();

                return ActionResult.SUCCESS;
            }

            if (getRecipeFor(stack).isPresent()&&items.get(0).getCount()<64) {
                if (!stack.getRecipeRemainder().isEmpty()){
                    player.giveItemStack(stack.getRecipeRemainder().split(1));
                }
                if (ItemStack.canCombine(stack,items.get(0))){
                    ItemStack stack1= items.get(0);
                    stack1.increment(1);
                    items.set(0,stack1);
                } else if (items.get(0).isEmpty()) {
                    items.set(0,stack.split(1));
                }

                stack.decrement(1);

                markDirty();

                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
    protected Optional<AlcoholBrewingRecipe> getRecipeFor(ItemStack stack){
        if (world == null || world.isClient){
            return Optional.empty();
        }
        return world.getServer().getRecipeManager().getFirstMatch(ModRecipes.alcoholRecipe,new SimpleInventory(stack),world);
    }
    protected ItemStack getProduct(){
        if (this.world.isClient){
            return ItemStack.EMPTY;
        }
        AlcoholBrewingRecipe recipe=getRecipeFor(items.get(0)).get();
        ItemStack stack=recipe.getOutput().copy();
        if (productAmount<=0){
            productAmount=stack.getCount();
        }

        if (time>=brewingTime&&time<=expirationTime){

            productAmount--;
            return stack.split(1);
        }else if(this.isExpired){
            reset();
            return ModItems.VINEGAR.getDefaultStack();
        }
        return Items.POTION.getDefaultStack();
    }

}
