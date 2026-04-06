package net.bobbacon2.item;

import net.bobbacon.item.ScrollItem;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon2.entity.ModEntities;
import net.bobbacon2.registry.ModRegistries;
import net.bobbacon2.status_effect.ModEffects;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.entity.ModEntities;
import net.bobbacon2.status_effect.ModEffects;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;

public class ModItems {
    private static final RegistryHelper<Item> registryHelper=new RegistryHelper<>(Registries.ITEM, NightOfTheDead.MOD_ID);
    private static final RegistryHelper<Potion> potionRegistryHelper=new RegistryHelper<>(Registries.POTION, NightOfTheDead.MOD_ID);
    public static final Item MOLOTOV= registryHelper.register("molotov_cocktail",new Molotov(new Item.Settings().maxCount(1),ModEntities.MOLOTOV));
    public static final Item FIERY_MOLOTOV= registryHelper.register("fiery_cocktail",new Molotov(new Item.Settings().maxCount(1),5,ModEntities.MOLOTOV));
    public static final Item NAPALM_MOLOTOV= registryHelper.register("napalm_cocktail",new Molotov(new Item.Settings().maxCount(1),4,ModEntities.NAPALM_MOLOTOV));
    public static final Item EXPLOSIVE_COCKTAIL= registryHelper.register("explosive_cocktail",new Molotov(new Item.Settings().maxCount(1),4,ModEntities.EXPLOSIVE_MOLOTOV));

    public static final Item VODKA = registryHelper.register("vodka",new Alcohol(new Item.Settings(),4,new StatusEffectInstance(StatusEffects.NAUSEA,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item MEAD = registryHelper.register("mead",new Alcohol(new Item.Settings(),4,new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item RUM = registryHelper.register("rum",new Alcohol(new Item.Settings(),4,new StatusEffectInstance(StatusEffects.LUCK,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item WHISKEY = registryHelper.register("whiskey",new Alcohol(new Item.Settings(),4,new StatusEffectInstance(StatusEffects.LUCK,300),new StatusEffectInstance(StatusEffects.RESISTANCE,300)));
    public static final Item BEER = registryHelper.register("beer",new Alcohol(new Item.Settings(),2,new StatusEffectInstance(StatusEffects.RESISTANCE,600)));
    public static final Item PURE_ALCOHOL = registryHelper.register("pure_alcohol",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.BLINDNESS,3000),new StatusEffectInstance(StatusEffects.WITHER,600),new StatusEffectInstance(StatusEffects.WEAKNESS,800)));
    public static final Item VINEGAR = registryHelper.register("vinegar",new Alcohol(new Item.Settings(),new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE)));

    public static final Item BREWING_BARREL = registryHelper.register("brewing_barrel", new BlockItem(net.bobbacon2.block.ModBlocks.BREWING_BARREL, new FabricItemSettings()));

    public static final Item BLOODY_WATER = registryHelper.register("bloody_water",new Item(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE)));
    //TODO faire en sorte que ce soit un alcool qui donne de la vie quand on est un vampire
    public static final Item BLOOD_BOTTLE = registryHelper.register("blood_bottle",new BloodBottle(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(ModEffects.INSANITY,300,1)).setVampireEffects(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE)));
    public static final Item CURSED_BLOOD_BOTTLE = registryHelper.register("cursed_blood_bottle",new BloodBottle(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(ModEffects.INSANITY,300,2),new StatusEffectInstance(StatusEffects.WITHER,300,2)).setVampireEffects(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE,0,5),new StatusEffectInstance(StatusEffects.STRENGTH,6000,1)));
    public static final Item FERMENTED_WATER = registryHelper.register("fermented_water",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(StatusEffects.HUNGER,200,0)));
    public static final Item SOBRIETY_POTION = registryHelper.register("sobriety_potion",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(ModEffects.SOBRIETY,1200,0)));
    public static final Item WITHERS_DRINK = registryHelper.register("withers_drink",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(StatusEffects.WITHER,7200,1),new StatusEffectInstance(ModEffects.ATTRITION,7200,1),new StatusEffectInstance(StatusEffects.STRENGTH,7200,3),new StatusEffectInstance(StatusEffects.NIGHT_VISION,7200,0),new StatusEffectInstance(StatusEffects.SPEED,7200,1)));
    public static final Item VAMPIRES_DRINK = registryHelper.register("vampires_drink",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(ModEffects.Vampiring,18000,0)));
    public static final Item TERRIBLE_MIX = registryHelper.register("terrible_mix",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(StatusEffects.WEAKNESS,1800,1),new StatusEffectInstance(StatusEffects.SLOWNESS,1800,1),new StatusEffectInstance(StatusEffects.POISON,1800,1),new StatusEffectInstance(StatusEffects.HUNGER,1800,1)));
    public static final Item ARTIFICIAL_MANA = registryHelper.register("artificial_mana",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(ModEffects.INSTANT_ARTIFICIAL_MANA),new StatusEffectInstance(ModEffects.ARTIFICIAL_MANA_BOOST,1800,1)));
    public static final Potion ATTRITION = potionRegistryHelper.register("attrition",new Potion(new StatusEffectInstance(ModEffects.ATTRITION,1200,1)));
    public static final Item METAL_SUPPORT = registryHelper.register("metal_support", new MetalSupportItem(ModEntities.METAL_SUPPORT, new Item.Settings()));
    public static final Item ALTAR = registryHelper.register("altar", new BlockItem(net.bobbacon2.block.ModBlocks.ALTAR, new FabricItemSettings()));
    public static final Item REFINERY = registryHelper.register("refinery", new BlockItem(ModBlocks.REFINERY, new FabricItemSettings()));
    public static final Item CORRUPTED_SHARD = registryHelper.register("corrupted_shard",new Item(new FabricItemSettings()));
    public static final Item VAMPIRITE = registryHelper.register("vampirite",new Item(new FabricItemSettings()));
    public static final Item SACRIFICIAL_DAGGER = registryHelper.register("sacrificial_dagger",new SacrificialDagger(ToolMaterials.NETHERITE,2, -3.0F,new FabricItemSettings()));



    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> {
                    entries.add(MOLOTOV);
                    entries.add(FIERY_MOLOTOV);
                    entries.add(NAPALM_MOLOTOV);
                    entries.add(EXPLOSIVE_COCKTAIL);
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register(entries -> {
                    entries.add(REFINERY);
                });
    }
}
