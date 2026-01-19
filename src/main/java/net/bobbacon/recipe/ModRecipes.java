package net.bobbacon.recipe;

import net.bobbacon.NightOfTheDead;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<AlcoholBrewingRecipe> alcoholBrewingRecipeSerializer=Registry.register(Registries.RECIPE_SERIALIZER, AlcoholBrewingRecipeSerializer.ID,
                                                           AlcoholBrewingRecipeSerializer.INSTANCE);
	public static final AlcoholBrewingRecipe.Type alcoholRecipe =  Registry.register(Registries.RECIPE_TYPE, Identifier.of(NightOfTheDead.MOD_ID, AlcoholBrewingRecipe.Type.ID), AlcoholBrewingRecipe.Type.INSTANCE);

    public static final RecipeSerializer<RefiningRecipe> RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(NightOfTheDead.MOD_ID, RefiningRecipe.Type.ID), RefiningRecipeSerializer.INSTANCE);
    public static final RefiningRecipe.Type refiningRecipe =  Registry.register(Registries.RECIPE_TYPE, Identifier.of(NightOfTheDead.MOD_ID, RefiningRecipe.Type.ID), RefiningRecipe.Type.INSTANCE);

    public static void init(){

    }
}
