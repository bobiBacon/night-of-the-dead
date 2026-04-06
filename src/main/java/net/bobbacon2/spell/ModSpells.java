package net.bobbacon2.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.spell.*;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.damage.ModDamageTypes;
import net.bobbacon2.registry.ModRegistries;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModSpells {
    private static final RegistryHelper<SpellDef<?>> registryHelper= new RegistryHelper<>(SpellRegistry.SPELL_TYPES, NightOfTheDead.MOD_ID);

    public static final SpellDef<?> CORRUPTION_RITUAL_SPELL = Registry.register(SpellRegistry.getSpellRegistry(), Identifier.of(NightOfTheDead.MOD_ID,"corruption_ritual"),new SpellDef<>(new CorruptionRitualSpell(), SpellSchools.Necromancy,20).useTinted2dSymbol(0xFF0000));
    public static final SpellDef<?> Punishment = registryHelper.register("punishment",new SpellDef<>(new PunishmentSpell(8,40),SpellSchools.Divine,40).setCooldown(200).useTinted2dSymbol(0xFFB900).setCastTime(20).customSymbolPath(new Identifier("minecraft","item/golden_sword")).withRenderer());
    public static final SpellDef<?> Exorcism = registryHelper.register("exorcism",new SpellDef<>(new InstantDamageSpell(15,2, ModDamageTypes.Holy, ParticleTypes.ELECTRIC_SPARK),SpellSchools.Divine,20).setCooldown(40).useTinted2dSymbol(0x751100).setCastTime(20));

    public static void init(){

    }
}
