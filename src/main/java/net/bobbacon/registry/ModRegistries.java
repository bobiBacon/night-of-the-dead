package net.bobbacon.registry;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ModRegistries {
    public static final RegistryKey<Registry<SpellType<?>>> SPELL_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(NightOfTheDead.MOD_ID, "spell"));
    public static final SimpleRegistry<SpellType<?>> SPELL_TYPES = FabricRegistryBuilder.createSimple(SPELL_REGISTRY_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    public static void init(){

    }
}
