package net.bobbacon.spell;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.registry.ModRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpellType<T extends Spell> {
    private static final RegistryHelper<SpellType<?>> registryHelper= new RegistryHelper<>(ModRegistries.SPELL_TYPES, NightOfTheDead.MOD_ID);

    public static final SpellType<?> CORRUPTION_RITUAL= registryHelper.register("corruption_ritual",new SpellType<>(CorruptionRitualSpell::new));
    public static final SpellType<?> EMPTY= registryHelper.register("empty",new SpellType<>(Spell::new));
    public static void init(){

    }

    private final SpellFactory<T> factory;


    public SpellType(SpellFactory<T> factory) {
        this.factory = factory;
    }
    public T create(World world){
        return factory.create(this,world);
    }

    public Identifier symbolTexture() {
        Identifier spellId= ModRegistries.SPELL_TYPES.getId(this);
        String path;
        String nameSpace;
        if (spellId==null){
            path="empty";
            nameSpace= NightOfTheDead.MOD_ID;
        }
        else {
            path= spellId.getPath();
            nameSpace= spellId.getNamespace();
        }
        return Identifier.of(nameSpace,"item/"+path);
    }

    public interface SpellFactory<T extends Spell> {
        T create(SpellType<T> type, World world);
    }
}
