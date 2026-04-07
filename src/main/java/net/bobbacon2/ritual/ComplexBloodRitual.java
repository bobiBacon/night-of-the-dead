package net.bobbacon2.ritual;

import net.bobbacon.ritual.Phase;
import net.bobbacon.ritual.Ritual;
import net.bobbacon2.enchants.ModEnchantments;
import net.bobbacon2.entity.MetalSupport;
import net.bobbacon2.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ComplexBloodRitual extends BasicBloodRitual{
    public ComplexBloodRitual(BlockPos center, World world) {
        super(center, world);
    }

    public ComplexBloodRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }
    public void definePhases(){
        phases.add(new ConsumeBlood());
        super.definePhases();
    }
    @Override
    public Ritual create(World world, NbtCompound nbtCompound) {
        return new ComplexBloodRitual(world,nbtCompound);
    }

    @Override
    public boolean hasRitualSite() {
        List<MetalSupport> supportsInArea = RitualUtils.getSupportsInArea(world, center, 6);
        boolean b=  false;
        for (MetalSupport support: supportsInArea){
            if (support.getHeldItemStack().isOf(ModItems.CURSED_BLOOD_BOTTLE)){
                b=true;
            }
        }

        return super.hasRitualSite()&&b;
    }

    @Override
    public boolean canAffect(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ToolItem toolItem){
            return toolItem.getMaterial()== ToolMaterials.NETHERITE&& EnchantmentHelper.getLevel(ModEnchantments.BLOOD_STEAL,stack)>=1;
        }
        return false;
    }

    @Override
    public ItemStack applyEffect(ItemStack stack) {
        stack.addEnchantment(ModEnchantments.Vampire,1);
        return stack;
    }
    public class ConsumeBlood implements Phase {

        @Override
        public void start() {
            RitualUtils.getSupportsInArea(world,center,6).forEach(metalSupport -> {
                if (metalSupport.getHeldItemStack().isOf(ModItems.CURSED_BLOOD_BOTTLE)){

                    metalSupport.setHeldItemStack(Items.GLASS_BOTTLE.getDefaultStack(),true);
                }
            });
        }

        @Override
        public boolean tick(int i) {
            return i>=40;
        }
    }
}
