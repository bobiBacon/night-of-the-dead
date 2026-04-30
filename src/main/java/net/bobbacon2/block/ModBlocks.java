package net.bobbacon2.block;

import net.bobbacon2.NightOfTheDead;
import net.bobbacon.api.RegistryHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {
    private static final RegistryHelper<Block> registryHelper=new RegistryHelper<>(Registries.BLOCK, NightOfTheDead.MOD_ID);
    public static final Block BREWING_BARREL = registryHelper.register("brewing_barrel",new BrewingBarrel(FabricBlockSettings.create().solid().sounds(BlockSoundGroup.WOOD).instrument(Instrument.BASS).strength(2.5F).burnable()));
    public static final Block ALTAR = registryHelper.register("altar",new Altar(FabricBlockSettings.create().requiresTool().sounds(BlockSoundGroup.STONE).instrument(Instrument.BASS).strength(2.5F).resistance(1200.0F)));
    public static final Block REFINERY = registryHelper.register("refinery",new RefineryBlock(FabricBlockSettings.create().requiresTool().sounds(BlockSoundGroup.STONE).instrument(Instrument.BASS).strength(2.5F)));
    public static final Block ANCIENT_PEDESTAL = registryHelper.register("ancient_pedestal",new AncienPedestal(FabricBlockSettings.create().requiresTool().sounds(BlockSoundGroup.STONE).instrument(Instrument.BASS).strength(2.5F).nonOpaque()));
    public static final Block ManaExtractor = registryHelper.register("mana_extractor",new ManaExtractorBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.STONE).instrument(Instrument.BASS).strength(0.5F).nonOpaque().notSolid().noCollision()));
    public static final Block SOUL_WORM_TUMOR = registryHelper.register("soul_worm_tumor",new SoulWormTumor(FabricBlockSettings.create().requiresTool().sounds(BlockSoundGroup.SCULK).instrument(Instrument.BASS).strength(2.5F).nonOpaque().notSolid()));
    public static final Block METAL_SUPPORT = registryHelper.register("metal_support",new Block(FabricBlockSettings.create()));
    public static final Block BLOOD_POOL = registryHelper.register("blood_pool",new BloodPoolBlock(FabricBlockSettings.create().requiresTool().sounds(BlockSoundGroup.METAL).strength(1.5F)));
    public static final Block ETERNAL_FIRE = registryHelper.register("eternal_fire",new EternalFireBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_BLUE)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .luminance(state -> 10)
            .sounds(BlockSoundGroup.WOOL)
            .pistonBehavior(PistonBehavior.DESTROY)
            .ticksRandomly()));

    public static void init(){

    }
}
