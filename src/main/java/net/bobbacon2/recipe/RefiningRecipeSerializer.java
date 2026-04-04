package net.bobbacon2.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.bobbacon2.NightOfTheDead;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class RefiningRecipeSerializer implements RecipeSerializer<RefiningRecipe> {
    public static final RefiningRecipeSerializer INSTANCE = new RefiningRecipeSerializer();



    public  RefiningRecipeSerializer() {
    }



    @Override
    public RefiningRecipe read(Identifier id, JsonObject json) {
        String group = JsonHelper.getString(json, "group", "");
        CraftingRecipeCategory craftingRecipeCategory = (CraftingRecipeCategory)CraftingRecipeCategory.CODEC
                .byId(JsonHelper.getString(json, "category", null), CraftingRecipeCategory.MISC);
        Map<String, Ingredient> map = ShapedRecipe.readSymbols(JsonHelper.getObject(json, "key"));
        String[] strings = ShapedRecipe.removePadding(ShapedRecipe.getPattern(JsonHelper.getArray(json, "pattern")));
        int i = strings[0].length();
        int j = strings.length;
        DefaultedList<Ingredient> ingredients = ShapedRecipe.createPatternMatrix(strings, map, i, j);
        ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
        boolean bl = JsonHelper.getBoolean(json, "show_notification", true);
        int cookingTime= JsonHelper.getInt(json,"cookingtime",250);
        double experience= JsonHelper.getDouble(json,"experience",0.5);
//        Item recipient= getItem(JsonHelper.getObject(json,"recipient",new JsonObject()),Items.AIR);
        Item recipient= getItem(JsonHelper.getString(json,"recipient",""),Items.AIR);
        return new RefiningRecipe(id,group,craftingRecipeCategory,i,j,ingredients,output,bl,cookingTime,experience,recipient);
    }
    public static Item getItem(JsonObject json,Item defaultItem) {
        String string = JsonHelper.getString(json, "item","");
        return getItem(string,defaultItem);
    }
    public static Item getItem(String string,Item defaultItem) {
        if (string.isEmpty()){
            return defaultItem;
        }
        Item item = Registries.ITEM.getOrEmpty(Identifier.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        return item;
    }

    @Override
    public RefiningRecipe read(Identifier id, PacketByteBuf buf) {
        int width = buf.readVarInt();
        int height = buf.readVarInt();
        String group = buf.readString();
        CraftingRecipeCategory craftingRecipeCategory = buf.readEnumConstant(CraftingRecipeCategory.class);
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);

        defaultedList.replaceAll(ignored -> Ingredient.fromPacket(buf));

        ItemStack itemStack = buf.readItemStack();
        boolean bl = buf.readBoolean();

        int cookingTime= buf.readInt();
        double experience= buf.readDouble();
        Item recipient= getItem(buf.readString(),Items.AIR);
        return new RefiningRecipe(id, group, craftingRecipeCategory, width, height, defaultedList, itemStack, bl,cookingTime,experience,recipient);
    }

    @Override
    public void write(PacketByteBuf buf, RefiningRecipe recipe) {
        new ShapedRecipe.Serializer().write(buf,recipe);
        buf.writeInt(recipe.cookingTime);
        buf.writeDouble(recipe.experience);
        buf.writeString(recipe.recipient.toString());
    }
}

