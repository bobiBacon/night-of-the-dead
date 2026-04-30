package net.bobbacon2.block;

import net.bobbacon.api.RegistryHelper;
import net.bobbacon2.NightOfTheDead;
import net.bobbacon2.evolution.Evolution;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class FakeFluids {
    private static final RegistryKey<Registry<FakeFluid>> FAKE_FLUID_KEY =
            RegistryKey.ofRegistry(new Identifier(NightOfTheDead.MOD_ID, "fake_fluid"));
    public static final SimpleRegistry<FakeFluid> FAKE_FLUIDS = FabricRegistryBuilder.createSimple(FAKE_FLUID_KEY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    private static final RegistryHelper<FakeFluid> registryHelper= new RegistryHelper<>(FAKE_FLUIDS, NightOfTheDead.MOD_ID);
    public static final FakeFluid MANA= registryHelper.register("mana",new FakeFluid(0x78F6FF));
    public static final FakeFluid CRUSHED_SOULS= registryHelper.register("crushed_souls",new FakeFluid(0x1C0036));
    public static final FakeFluid LIQUID_EVIL= registryHelper.register("liquid_evil",new FakeFluid(0x450000));
    public static final FakeFluid BLOOD= registryHelper.register("blood",new FakeFluid(0x220000));
    public static final FakeFluid EMPTY= registryHelper.register("empty",new FakeFluid(0xFFFFFF));
    public static void init(){

    }
}
