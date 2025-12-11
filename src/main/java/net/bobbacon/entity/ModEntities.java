package net.bobbacon.entity;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon.item.Molotov;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<MolotovEntity> MOLOTOV = register(
            "molotov_entity", FabricEntityTypeBuilder.<MolotovEntity>create( SpawnGroup.MISC, MolotovEntity::new).dimensions(new EntityDimensions(0.5f,0.5f,true))
    );
    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id, type.build());
    }
    public static void init(){

    }
}
