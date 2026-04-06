package net.bobbacon2.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class ExplosiveMolotov extends MolotovEntity{
    public ExplosiveMolotov(double d, double e, double f, World world, int explosionRadius) {
        super(d, e, f, world, explosionRadius);
    }

    public ExplosiveMolotov(LivingEntity livingEntity, World world, int explosionRadius) {
        super(livingEntity, world, explosionRadius);
    }

    public ExplosiveMolotov(EntityType<? extends MolotovEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void explode(double colx, double coly, double colz) {
        World world = getWorld();
        if (world.isClient){
            return;
        }
        spawnExplosionParticles(world,colx,coly,colz,false);

        playBreakingSounds(world);
        world.createExplosion(this,colx,coly,colz,2,true, World.ExplosionSourceType.TNT);


        this.discard();
    }
}
