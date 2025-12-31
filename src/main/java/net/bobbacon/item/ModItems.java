package net.bobbacon.item;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.registry.ModRegistries;
import net.bobbacon.spell.SpellType;
import net.bobbacon.status_effect.ModEffects;
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

    public static final Item BLOODY_WATER = registryHelper.register("bloody_water",new Item(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE)));
    //TODO faire en sorte que ce soit un alcool qui donne de la vie quand on est un vampire
    public static final Item BLOOD_BOTTLE = registryHelper.register("blood_bottle",new Alcohol(new FabricItemSettings().recipeRemainder(Items.GLASS_BOTTLE),new StatusEffectInstance(ModEffects.INSANITY,300,1)));
    public static final Potion ATTRITION = potionRegistryHelper.register("attrition",new Potion(new StatusEffectInstance(ModEffects.ATTRITION,1200,1)));
    public static final Item METAL_SUPPORT = registryHelper.register("metal_support", new MetalSupportItem(ModEntities.METAL_SUPPORT, new Item.Settings()));
    public static final Item SCROLL = registryHelper.register("scroll", new ScrollItem(new Item.Settings()));
    public static final Item ALTAR = registryHelper.register("altar", new BlockItem(ModBlocks.ALTAR, new FabricItemSettings()));
    public static final Item CORRUPTED_SHARD = registryHelper.register("corrupted_shard",new Item(new FabricItemSettings()));



    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(entries -> {
                    for (SpellType<?> spell : ModRegistries.SPELL_TYPES) {
                        if (!spell.isEmpty()){
                            ItemStack stack = new ItemStack(ModItems.SCROLL);
                            ScrollItem.setSpell(stack, spell);
                            entries.add(stack);
                        }else {
                            entries.add(ModItems.SCROLL.getDefaultStack());
                        }
                    }
                });
    }
}
