package net.bobbacon2.entity;

import net.bobbacon2.NightOfTheDead;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SoulWormPart extends MobEntity {
    private final EntityDimensions partDimensions;
    private float scale = 1.0f;
    Vec3d startingPoint;
    Vec3d endingPoint;
    SoulWorm parent;

    protected SoulWormPart(SoulWorm parent, Vec3d startingPoint) {
        super(ModEntities.SOUL_WORM_PART, parent.getWorld());
        this.parent=parent;
        this.startingPoint=startingPoint;
        this.endingPoint=startingPoint.add(0,0,-1);
        this.partDimensions = EntityDimensions.changing(1, 0.4f);
        this.calculateDimensions();

    }

    public SoulWormPart(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        this.partDimensions = EntityDimensions.changing(1, 0.4f);
        this.calculateDimensions();
        this.startingPoint=new Vec3d(0,0,0);
        this.endingPoint=startingPoint.add(0,0,-1);
    }

    public void setParent(SoulWorm parent) {
        this.parent = parent;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        Vec3d pos=startingPoint.lerp(endingPoint,0.5);
        setPos(pos.getX(),pos.getY(),pos.getZ());
        prevX = getX();
        prevY = getY();
        prevZ = getZ();
//        NightOfTheDead.LOGGER.info(String.valueOf(pos));
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.calculateDimensions();
    }


    @Override
    public Iterable<ItemStack> getArmorItems() {
        return new ArrayList<>();
    }



    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }


    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return partDimensions;
    }
    @Override
    public boolean canHit() {
        return true;
    }

    @Override
     public void readCustomDataFromNbt(NbtCompound nbt) {
        if (getWorld().isClient()){
            return;
        }
        ServerWorld world= (ServerWorld) getWorld();
        if (nbt.contains("parent_id")){

            parent= (SoulWorm) world.getEntity(nbt.getUuid("parent_id"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putUuid("parent_id",parent.getUuid());
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return this.isInvulnerableTo(source)&&!getWorld().isClient()? false : this.parent.damage(source, amount);
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return this == entity || this.parent == entity;
    }
    @Override
    public boolean collidesWith(Entity other) {
        return !(other instanceof SoulWormPart);
    }
    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
}
