package net.bobbacon.item;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ModItems {
    private static final RegistryHelper<Item> registryHelper=new RegistryHelper<>(Registries.ITEM, NightOfTheDead.MOD_ID);
    public static final Item MOLOTOV= registryHelper.register("molotov_cocktail",new Molotov(new Item.Settings().maxCount(1)));
    public static final Item FIERY_MOLOTOV= registryHelper.register("fiery_cocktail",new Molotov(new Item.Settings().maxCount(1),5));

    public static final Item VODKA = registryHelper.register("vodka",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.NAUSEA,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item MEAD = registryHelper.register("mead",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item RUM = registryHelper.register("rum",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.LUCK,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item WHISKEY = registryHelper.register("whiskey",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.LUCK,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item BEER = registryHelper.register("beer",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.RESISTANCE,600)));
    public static final Item PURE_ALCOHOL = registryHelper.register("pure_alcohol",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.BLINDNESS,3000),new StatusEffectInstance(StatusEffects.WITHER,600),new StatusEffectInstance(StatusEffects.WEAKNESS,800)));
    public static final Item VINEGAR = registryHelper.register("vinegar",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE)));

    public static final Item BREWING_BARREL = registryHelper.register("brewing_barrel", new BlockItem(ModBlocks.BREWING_BARREL, new FabricItemSettings()));


    public static void init(){

    }
}
