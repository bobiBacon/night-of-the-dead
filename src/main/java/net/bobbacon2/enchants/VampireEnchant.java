package net.bobbacon2.enchants;

import net.bobbacon2.accessors.PlayerAccessor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class VampireEnchant extends Enchantment {
    protected VampireEnchant() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }


    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }
    public static void onEntityHit(float damage, LivingEntity attacker,LivingEntity target, DamageSource damageSource) {
        for (int i = 0; i < 20; i++) {
            World world = attacker.getWorld();
            Vec3d targetPos= new Vec3d(target.getParticleX(0.7),target.getRandomBodyY(),target.getParticleZ(0.7));
            Random random = world.getRandom();
            Vec3d toAttacker= attacker.getEyePos().add(random.nextFloat()*0.4f-0.2f,random.nextFloat()*0.4f-0.2f,random.nextFloat()*0.4f-0.2f).subtract(targetPos).normalize();
            ((ServerWorld) world).spawnParticles(new DustParticleEffect(Vec3d.unpackRgb(0x880808).toVector3f(),1),targetPos.getX() ,targetPos.getY(),targetPos.getZ(),1,toAttacker.getX(),toAttacker.getY(),toAttacker.getZ(),0);
        }
        float multiplicator= 0.20f;
        if (attacker instanceof PlayerEntity player&&((PlayerAccessor)player).isVampire()){
            multiplicator=0.40f;
        }
        attacker.heal(damage *multiplicator);
    }

}
