package net.bobbacon2.sound;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon2.NightOfTheDead;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final RegistryHelper<SoundEvent> registryHelper= new RegistryHelper<>(Registries.SOUND_EVENT, NightOfTheDead.MOD_ID);
    public static final SoundEvent ANCIENT_PEDESTAL_CHARGE= register("block.ancient_pedestal_charge");
    public static final SoundEvent ANCIENT_PEDESTAL_FINAL_CHARGE= register("block.ancient_pedestal_final_charge");

    public static SoundEvent register(String name){
        Identifier id= Identifier.of(NightOfTheDead.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void init(){

    }
}
