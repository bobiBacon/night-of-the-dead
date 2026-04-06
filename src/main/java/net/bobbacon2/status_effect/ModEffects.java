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
    public static final StatusEffect SOBRIETY = registryHelper.register("sobriety",new Sobriety(StatusEffectCategory.BENEFICIAL, 0x0033FF));
    public static final StatusEffect Vampiring = registryHelper.register("vampiring",new Vampiring(StatusEffectCategory.NEUTRAL, 0x7D002C));
    public static final StatusEffect ARTIFICIAL_MANA_BOOST = registryHelper.register("artificial_mana_boost",new ArtificialManaBoost(StatusEffectCategory.BENEFICIAL, 0x450044));
    public static final StatusEffect INSTANT_ARTIFICIAL_MANA = registryHelper.register("instant_artificial_mana",new InstantArtificialMana(StatusEffectCategory.BENEFICIAL, 0x450044));

    public static void init(){

    }
}
