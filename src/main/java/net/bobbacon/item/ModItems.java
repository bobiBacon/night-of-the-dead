package net.bobbacon.item;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ModItems {
    private static final RegistryHelper<Item> registryHelper=new RegistryHelper<>(Registries.ITEM, NightOfTheDead.MOD_ID);
    public static final Item MOLOTOV= registryHelper.register("molotov_cocktail",new Molotov(new Item.Settings()));

    public static void init(){

    }
}
