package net.bobbacon2.damage;

import net.bobbacon2.NightOfTheDead;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> Holy = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(NightOfTheDead.MOD_ID, "holy"));
    public static void init(){

    }
}
