package net.bobbacon2.render.spell;

import net.bobbacon.TheSpellLibrary;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.render.spell.*;
import net.bobbacon.spell.SpellDef;
import net.bobbacon.spell.SpellDefs;
import net.bobbacon2.spell.ModSpells;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModSpellRenderers {

    private static final RegistryHelper<SpellRendererFactory> registryHelper= new RegistryHelper<>(SpellRenderers.SPELL_RENDERER,TheSpellLibrary.MOD_ID);
    public static final SpellRendererFactory PUNISHMENT_RENDERER= registryHelper.register("punishment", PunishmentSpellRenderer::new);

    public static void init(){
        SpellRenderers.bindRenderer(ModSpells.Punishment,PUNISHMENT_RENDERER);
    }

}
