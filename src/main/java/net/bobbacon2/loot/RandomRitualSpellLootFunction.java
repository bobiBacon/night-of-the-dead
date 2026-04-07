package net.bobbacon2.loot;

import com.google.gson.*;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.loot.ModLoot;
import net.bobbacon.loot.Predicates;
import net.bobbacon.loot.SpellLootModifier;
import net.bobbacon.loot.SpellLootModifiers;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.bobbacon.utils.Utils;
import net.bobbacon2.spell.ModSpells;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Rarity;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class RandomRitualSpellLootFunction extends ConditionalLootFunction {
    protected final Predicate<LootContext> predicate;

    protected RandomRitualSpellLootFunction(LootCondition[] conditions, Predicate<LootContext> predicate) {
        super(conditions);
        if (predicate==null){
            predicate= Predicates.AlwaysTrueLoot;
        }
        this.predicate = predicate;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
       if (!predicate.test(context)){
           return ItemStack.EMPTY;
       }



        SpellDef<?>[] list= {ModSpells.CORRUPTION_RITUAL_SPELL,ModSpells.SIMPLE_BLOOD_RITUAL_SPELL};
        Map<SpellDef<?>,Integer> pool = new HashMap<>();
        for (SpellDef<?> def:list){
            pool.put(def,10000);
        }


        SpellDef<?> spell = Utils.weightedRandom(pool);
        ScrollItem.setSpell(stack, spell);

        return stack;
    }

    public static LootFunction.Builder builder(Predicate<LootContext> predicate) {
        return builder((Function<LootCondition[], LootFunction>) conditions -> new RandomRitualSpellLootFunction(conditions, predicate));
    }

    @Override
    public LootFunctionType getType() {
        return ModLoot.RANDOM_SPELL;
    }
    public static class Serializer extends ConditionalLootFunction.Serializer<RandomRitualSpellLootFunction> {
        public void toJson(JsonObject jsonObject, RandomRitualSpellLootFunction randomSpellFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, randomSpellFunction, jsonSerializationContext);
            jsonObject.addProperty("predicate", Predicates.PREDICATES.getId(randomSpellFunction.predicate).toString());
        }

        public RandomRitualSpellLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            String string = JsonHelper.getString(jsonObject, "predicate",null);
            Predicate<?> predicate = string==null? Predicates.AlwaysTrueLoot:Predicates.PREDICATES
                    .getOrEmpty(Identifier.tryParse(string))
                    .orElseThrow(() -> new JsonSyntaxException("Unknown condition '" + string + "'"));
            return new RandomRitualSpellLootFunction(lootConditions, (Predicate<LootContext>) predicate);
        }
    }
}
