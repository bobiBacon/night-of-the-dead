package net.bobbacon2.entity;

import net.bobbacon2.utils.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NapalmMolotovEntity extends MolotovEntity{
    public NapalmMolotovEntity(double d, double e, double f, World world, int explosionRadius) {
        super(d, e, f, world, explosionRadius);
    }

    public NapalmMolotovEntity(LivingEntity livingEntity, World world, int explosionRadius) {
        super(livingEntity, world, explosionRadius);
    }

    public NapalmMolotovEntity(EntityType<? extends MolotovEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void explode(double colx, double coly, double colz) {
        World world= getWorld();
        if (world instanceof ServerWorld serverWorld){
            Utils.lightOnFire(Utils.getCircle(new BlockPos((int) colx, (int) coly, (int) colz),explosionRadius),serverWorld,true);
        }
        spawnExplosionParticles(world,colx,coly,colz,true);
        playBreakingSounds(world);
        this.discard();

    }
}
