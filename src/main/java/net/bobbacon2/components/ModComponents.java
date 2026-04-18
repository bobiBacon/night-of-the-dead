package net.bobbacon2.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.components.ManaComponent;
import net.bobbacon2.NightOfTheDead;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<EvolutionComponent> EVOLUTION_COMPONENT =
            ComponentRegistry.getOrCreate(
                    new Identifier(NightOfTheDead.MOD_ID, "evolution"),
                    EvolutionComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                EVOLUTION_COMPONENT,
                EvolutionComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}
