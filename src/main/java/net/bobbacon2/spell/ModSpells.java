package net.bobbacon2.spell;

import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.registry.ModRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModSpells {
    public static final SpellDef<?> CORRUPTION_RITUAL_SPELL = Registry.register(SpellRegistry.getSpellRegistry(), Identifier.of(NightOfTheDead.MOD_ID,"corruption_ritual"),new SpellDef<>(new CorruptionRitualSpell(),20).useTinted2dSymbol(0xFF0000));
    public static void init(){

    }
}
