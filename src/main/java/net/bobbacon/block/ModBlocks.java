package net.bobbacon.block;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ModBlocks {
    private static final RegistryHelper<Block> registryHelper=new RegistryHelper<>(Registries.BLOCK, NightOfTheDead.MOD_ID);
    public static final Block BREWING_BARREL = registryHelper.register("brewing_barrel",new BrewingBarrel(FabricBlockSettings.create().solid()));

    public static void init(){

    }
}
