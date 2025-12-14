package net.bobbacon.entity.block_entity;

import net.bobbacon.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBE {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("tutorial", path), blockEntityType);
    }

    public static final BlockEntityType<BrewingBarrelBE> BREWING_BARREL_BE = register(
            "demo_block",
            BlockEntityType.Builder.create(BrewingBarrelBE::new, ModBlocks.BREWING_BARREL).build(null)
    );
    public static void init(){

    }
}
