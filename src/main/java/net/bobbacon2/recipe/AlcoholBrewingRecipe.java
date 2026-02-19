package net.bobbacon2.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AlcoholBrewingRecipe implements Recipe<Inventory> {
    private final Ingredient input;
    private final ItemStack result;
    private final Identifier id;

    public AlcoholBrewingRecipe(Ingredient input, ItemStack result, Identifier id) {
        this.input = input;
        this.result = result;
        this.id = id;
    }
    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return input.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.result;
    }
    public ItemStack getOutput() {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AlcoholBrewingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<AlcoholBrewingRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "alcohol_brewing_recipe";
    }

    class JsonFormat {
        JsonObject input;
        String outputItem;
        int outputAmount;
    }
}
