package net.bobbacon2.entity;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.bobbacon2.item.Molotov;
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
    public static final EntityType<NapalmMolotovEntity> NAPALM_MOLOTOV = register(
            "napalm_molotov_entity", FabricEntityTypeBuilder.<NapalmMolotovEntity>create( SpawnGroup.MISC, NapalmMolotovEntity::new).dimensions(new EntityDimensions(0.5f,0.5f,true))
    );
    public static final EntityType<ExplosiveMolotov> EXPLOSIVE_MOLOTOV = register(
            "explosive_molotov_entity", FabricEntityTypeBuilder.<ExplosiveMolotov>create( SpawnGroup.MISC, ExplosiveMolotov::new).dimensions(new EntityDimensions(0.5f,0.5f,true))
    );
    public static final EntityType<FireDrop> FIRE_DROP = register(
            "fire_drop_entity", FabricEntityTypeBuilder.<FireDrop>create( SpawnGroup.MISC, FireDrop::new).dimensions(new EntityDimensions(0.2f,0.2f,true))
    );
    public static final EntityType<MetalSupport> METAL_SUPPORT = register(
            "metal_support", FabricEntityTypeBuilder.<MetalSupport>create( SpawnGroup.MISC, MetalSupport::new).dimensions(new EntityDimensions(0.2f,0.4f,true))
    );

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(NightOfTheDead.MOD_ID,id), type.build());
    }
    public static void init(){

    }
}
