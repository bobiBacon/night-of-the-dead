package net.bobbacon.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.bobbacon.NightOfTheDead;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class AlcoholBrewingRecipeSerializer implements RecipeSerializer<AlcoholBrewingRecipe> {
    public static final AlcoholBrewingRecipeSerializer INSTANCE = new AlcoholBrewingRecipeSerializer();
    public static final Identifier ID = Identifier.of(NightOfTheDead.MOD_ID,"alcohol_brewing");
    @Override
    public AlcoholBrewingRecipe read(Identifier id, JsonObject json) {
        NightOfTheDead.LOGGER.info("loading recipe: " + id);
        AlcoholBrewingRecipe.JsonFormat recipeJson = new Gson().fromJson(json, AlcoholBrewingRecipe.JsonFormat.class);

        if (recipeJson.input == null || recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
        if (recipeJson.outputAmount == 0) recipeJson.outputAmount = 1;
        Ingredient input = Ingredient.fromJson(recipeJson.input);

        Item outputItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
                .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new AlcoholBrewingRecipe(input, output, id);
    }

    @Override
    public AlcoholBrewingRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient input = Ingredient.fromPacket(buf);
        ItemStack output = buf.readItemStack();
        return new AlcoholBrewingRecipe(input,output, id);
    }

    @Override
    public void write(PacketByteBuf buf, AlcoholBrewingRecipe recipe) {
        recipe.getInput().write(buf);
        buf.writeItemStack(recipe.getOutput());
    }
}
