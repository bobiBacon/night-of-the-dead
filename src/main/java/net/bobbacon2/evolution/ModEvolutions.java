package net.bobbacon2.evolution;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.spell.SpellDef;
import net.bobbacon2.NightOfTheDead;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ModEvolutions {
    private static final RegistryKey<Registry<Evolution>> EVOLUTION_KEY =
            RegistryKey.ofRegistry(new Identifier(TheSpellLibrary.MOD_ID, "evolution"));
    public static final SimpleRegistry<Evolution> EVOLUTIONS = FabricRegistryBuilder.createSimple(EVOLUTION_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    private static final RegistryHelper<Evolution> registryHelper= new RegistryHelper<>(EVOLUTIONS, NightOfTheDead.MOD_ID);
    public static final Evolution EMPTY= registryHelper.register("empty",new DefaultEvolution());

    public static final Evolution VAMPIRE= registryHelper.register("vampire",new Vampire());
    public static void init(){

    }
}
