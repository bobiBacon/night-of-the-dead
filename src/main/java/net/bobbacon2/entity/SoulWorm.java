package net.bobbacon2.entity;

import net.bobbacon2.NightOfTheDead;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoulWorm extends PathAwareEntity implements Monster {
    List<SoulWormPart> parts= new ArrayList<>();
    ArrayList<Vec3d> trailPoints=new ArrayList<>();
    protected SoulWorm(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.trailPoints.add(this.getPos());

    }
    public SoulWorm(World world, double x, double y, double z) {
        super(ModEntities.SOUL_WORM, world);
        this.setPosition(x, y, z);
        this.trailPoints.add(this.getPos());
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

    }

    @Override
    public void tick() {
        super.tick();
        if (parts.isEmpty())return;
        parts.get(0).startingPoint=this.getPos();
        for (int i = 0; i < parts.size()-1; i++) {
            Vec3d toNextPoint=parts.get(i).startingPoint.subtract(parts.get(i+1).startingPoint);
            Vec3d part=toNextPoint.normalize().multiply(parts.get(i).getScale());
            parts.get(i).endingPoint=parts.get(i).startingPoint.subtract(part);
            parts.get(i+1).startingPoint=parts.get(i).endingPoint;

        }

        Vec3d toNextPoint=parts.get(parts.size()-1).startingPoint.subtract(parts.get(parts.size()-1).endingPoint);
        Vec3d part=toNextPoint.normalize().multiply(parts.get(parts.size()-1).getScale());
        parts.get(parts.size()-1).endingPoint=parts.get(parts.size()-1).startingPoint.subtract(part);
        for (int i = 0; i < parts.size(); i++) {
//            NightOfTheDead.LOGGER.info("part "+i);
            parts.get(i).baseTick();
            parts.get(i).calculateDimensions();
        }
//        parts.forEach(SoulWormPart::baseTick);


        Vec3d lastPos= trailPoints.get(trailPoints.size()-1);
        double distance= getPos().distanceTo(lastPos);
        if (distance>=0.5){
            trailPoints.add(getPos());
        }
        if (getWorld().isClient()){
            return;
        }
        ServerWorld world= (ServerWorld) getWorld();
        for (var point:trailPoints){
            world.spawnParticles(ParticleTypes.FLAME,point.getX(),point.getY(),point.getZ(),1,0,0,0,0);
        }
    }

    public List<Vec3d> buildSegmentPositions(List<Vec3d> trailPoints, int segmentCount, float segmentLength) {
        List<Vec3d> result = new ArrayList<>();

        if (trailPoints.size() < 2) return result;

        float targetDistance = segmentLength;
        float currentDistance = 0f;

        for (int i = 1; i < trailPoints.size(); i++) {
            Vec3d a = trailPoints.get(i - 1);
            Vec3d b = trailPoints.get(i);

            float segmentDist = (float) a.distanceTo(b);

            while (currentDistance + segmentDist >= targetDistance) {
                float t = (targetDistance - currentDistance) / segmentDist;

                Vec3d point = a.lerp(b, t);
                result.add(point);

                if (result.size() >= segmentCount) {
                    return result;
                }

                targetDistance += segmentLength;
            }

            currentDistance += segmentDist;
        }

        return result;
    }


    @Override
    public Iterable<ItemStack> getArmorItems() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    public List<SoulWormPart> getParts() {
        return parts;
    }
    @Override
    public boolean canHit() {
        return false;
    }
    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);

        for (int i = 0; i < parts.size(); i++) {
            parts.get(i).setId(i + packet.getId());
        }
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        SoulWormPart e = new SoulWormPart(this, this.getPos());
        world.spawnEntity(e);
        e.setParent(this);
        parts.add(e);
        SoulWormPart e1 = new SoulWormPart(this, this.getPos().add(0, 0, -1));
        world.spawnEntity(e1);
        e1.setParent(this);

        parts.add(e1);
        SoulWormPart e2 = new SoulWormPart(this, this.getPos().add(0, 0, -2));
        world.spawnEntity(e2);
        e2.setParent(this);

        parts.add(e2);
        SoulWormPart e3 = new SoulWormPart(this, this.getPos().add(0, 0, -3));
        world.spawnEntity(e3);
        e3.setParent(this);

        parts.add(e3);
        SoulWormPart e4 = new SoulWormPart(this, this.getPos().add(0, 0, -4));
        world.spawnEntity(e4);
        e4.setParent(this);

        parts.add(e4);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        NbtList list = new NbtList();

        for (var part : parts) {
            list.add(NbtHelper.fromUuid(part.getUuid()));
        }

        nbt.put("Segments", list);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (getWorld().isClient()){
            return;
        }
        parts.clear();

        NbtList list = nbt.getList("Segments", NbtElement.INT_ARRAY_TYPE);

        for (int i = 0; i < list.size(); i++) {
            parts.add((SoulWormPart) ((ServerWorld)getWorld()).getEntity(NbtHelper.toUuid(list.get(i))));
        }
    }
}
