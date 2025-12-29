package net.bobbacon.spell;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.registry.ModRegistries;
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

    public Identifier symbolTextureFor2d() {
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
    public Identifier getId(){
        return ModRegistries.SPELL_TYPES.getId(this);
    }
    public Identifier getModelId() {
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
        return Identifier.of(nameSpace,path);
    }
    public Identifier symbolTextureFor3d(){
        Identifier base = symbolTextureFor2d();
        return Identifier.of(base.getNamespace(),base.getPath()+"_3d");
    }

    public interface SpellFactory<T extends Spell> {
        T create(SpellType<T> type, World world);
    }
}
