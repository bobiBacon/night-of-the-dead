package net.bobbacon.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class RefiningRecipe extends ShapedRecipe implements CraftingRecipe {
    int cookingTime;
    double experience;
    Item recipient;
    public RefiningRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output, boolean showNotification, int cookingTime,double experience, Item recipient) {
        super(id, group, category, width, height, input, output, showNotification);
        this.cookingTime= cookingTime;
        this.experience= experience;
        this.recipient= recipient;
    }

    public RefiningRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output, int cookingTime,double experience, Item recipient) {
        this(id, group, category, width, height, input, output,true,cookingTime,experience,recipient);
    }
    public static class Type implements RecipeType<AlcoholBrewingRecipe> {
        private Type() {}
        public static final RefiningRecipe.Type INSTANCE = new RefiningRecipe.Type();
        public static final String ID = "refining_recipe";
    }
}
