package net.bobbacon2.entity.block_entity;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBE {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(NightOfTheDead.MOD_ID, path), blockEntityType);
    }

    public static final BlockEntityType<BrewingBarrelBE> BREWING_BARREL_BE = register(
            "brewing_barrel",
            BlockEntityType.Builder.create(BrewingBarrelBE::new, net.bobbacon2.block.ModBlocks.BREWING_BARREL).build(null)
    );
    public static final BlockEntityType<AltarBE> ALTAR_BE = register(
            "altar",
            BlockEntityType.Builder.create(AltarBE::new, net.bobbacon2.block.ModBlocks.ALTAR).build(null)
    );
    public static final BlockEntityType<Refinery> REFINERY= register(
            "refinery",
            BlockEntityType.Builder.create(Refinery::new, ModBlocks.REFINERY).build(null)
    );
    public static final BlockEntityType<BloodPool> BLOOD_POOL= register(
            "blood_pool",
            BlockEntityType.Builder.create(BloodPool::new, ModBlocks.BLOOD_POOL).build(null)
    );
    public static final BlockEntityType<ManaExtractor> MANA_EXTRACTOR= register(
            "mana_extractor",
            BlockEntityType.Builder.create(ManaExtractor::new, ModBlocks.ManaExtractor).build(null)
    );
    public static void init(){

    }
}
