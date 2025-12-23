package net.bobbacon.block;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {
    private static final RegistryHelper<Block> registryHelper=new RegistryHelper<>(Registries.BLOCK, NightOfTheDead.MOD_ID);
    public static final Block BREWING_BARREL = registryHelper.register("brewing_barrel",new BrewingBarrel(FabricBlockSettings.create().solid().sounds(BlockSoundGroup.WOOD).instrument(Instrument.BASS).strength(2.5F).burnable()));
    public static final Block METAL_SUPPORT = registryHelper.register("metal_support",new Block(FabricBlockSettings.create()));

    public static void init(){

    }
}
