package net.bobbacon2.enchants;

import net.bobbacon2.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BloodSteal extends Enchantment {
    protected BloodSteal() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
    @Override
    public int getMinPower(int level) {
        return 15;
    }

    @Override
    public int getMaxPower(int level) {
        return 60;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!user.getWorld().isClient()&&target instanceof LivingEntity entity&& !entity.isAlive()){
            ItemStack stack=user.getOffHandStack();
            if (user instanceof PlayerEntity player&&stack.isOf(Items.GLASS_BOTTLE)){
                stack.decrement(1);
                player.giveItemStack(ModItems.BLOOD_BOTTLE.getDefaultStack());
            }
        }
        super.onTargetDamaged(user, target, level);
    }
}
