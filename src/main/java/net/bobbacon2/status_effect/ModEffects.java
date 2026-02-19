package net.bobbacon2.status_effect;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ModEffects {
    private static final RegistryHelper<StatusEffect> registryHelper=new RegistryHelper<>(Registries.STATUS_EFFECT, NightOfTheDead.MOD_ID);
    public static final StatusEffect ATTRITION = registryHelper.register("attrition",new Attrition(StatusEffectCategory.HARMFUL, 0xb31010));
    public static final StatusEffect INSANITY = registryHelper.register("insanity",new Insanity(StatusEffectCategory.HARMFUL, 0x70076d));

    public static void init(){

    }
}
