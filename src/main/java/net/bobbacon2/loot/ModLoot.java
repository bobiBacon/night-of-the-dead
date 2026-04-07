package net.bobbacon2.loot;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.loot.RandomSpellLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;

public class ModLoot {
    public static final LootFunctionType RANDOM_RITUAL_SPELL= register("random_ritual_spell", new RandomRitualSpellLootFunction.Serializer());
    private static LootFunctionType register(String id, JsonSerializer<? extends LootFunction> jsonSerializer) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE, Identifier.of(TheSpellLibrary.MOD_ID,id), new LootFunctionType(jsonSerializer));
    }
    public static void init(){

    }
}
