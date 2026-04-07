package net.bobbacon2.enchants;

import net.bobbacon.api.RegistryHelper;
import net.bobbacon2.NightOfTheDead;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

public class ModEnchantments{
    private static final RegistryHelper<Enchantment> registryHelper= new RegistryHelper<>(Registries.ENCHANTMENT, NightOfTheDead.MOD_ID);
    public static final Enchantment BLOOD_STEAL = registryHelper.register("blood_steal",new BloodSteal());
    public static final Enchantment Vampire = registryHelper.register("vampire",new Vampire());
    public static void init(){

    }
}
