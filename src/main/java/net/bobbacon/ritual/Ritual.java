package net.bobbacon.ritual;

import net.bobbacon.NightOfTheDead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Ritual {
    private static final String ID_KEY= "id";
    public UUID id;
    public BlockPos center;
    private static final String CENTER_X_KEY = "x";
    private static final String CENTER_Y_KEY = "y";
    private static final String CENTER_Z_KEY = "z";

    public World world;
    private static final String STARTED_KEY = "started";
    public boolean started= false;
    private static final String PHASE_KEY = "phase";
    byte phase= 0;
    private static final String TIME_KEY="time";
    public int time=0;
    public int maxPhases=0;
    private static final String IS_PHASE_INIT_KEY= "is_phase_init";
    boolean isPhaseInit = false;
    private static final String ENTITY_LIST_KEY = "entities";
    public final ArrayList<UUID> entities= new ArrayList<>();
    /**Only for optimisation.
     * For real check, please take a look to areEntitiesAlive().*/
    protected int entityCount=0;





    public Ritual(BlockPos center, World world) {
        this.center = center;
        this.world = world;
        this.id= UUID.randomUUID();
        markDirty();
    }
    public Ritual(World world, NbtCompound nbt){
        this.world = world;

        readNbt(nbt);
    }

    /**only evaluated on server side*/
    protected void tick(){
        if (started){
            this.time++;

        }
    }

    public abstract boolean hasRitualSite();
    public boolean tryStart(){
        if (world.isClient){
            return false;
        }
        started = hasRitualSite();
        if (started){
            RitualManager ritualManager = RitualManager.get((ServerWorld) world);
            ritualManager.add(this);
            NightOfTheDead.LOGGER.info("triggered on start");
            nextPhase();
            NightOfTheDead.LOGGER.info("Starting ritual");
        }
        return started;
    }
    public void nextPhase(){
        if (started){
            NightOfTheDead.LOGGER.info("changing phase");
            if (phase<maxPhases){
                phase++;
                isPhaseInit=false;
            }else {
                complete();
            }
            markDirty();
        }
    }

    protected void complete() {
        if (world.isClient){
            return;
        }
        NightOfTheDead.LOGGER.info("finished ritual");
        abort();
    }
    /**Terminates the ritual whenever complete or not*/
    public void abort(){
        RitualManager.get((ServerWorld) world).remove(this);
    }

    public void readNbt(NbtCompound nbt) {
        id= nbt.getUuid(ID_KEY);
        phase= nbt.getByte(PHASE_KEY);
        started= nbt.getBoolean(STARTED_KEY);
        center= new BlockPos(nbt.getInt(CENTER_X_KEY),nbt.getInt(CENTER_Y_KEY),nbt.getInt(CENTER_Z_KEY));
        time= nbt.getInt(TIME_KEY);
        isPhaseInit=nbt.getBoolean(IS_PHASE_INIT_KEY);
        NbtList list= nbt.getList(ENTITY_LIST_KEY, NbtElement.STRING_TYPE);
        for (NbtElement e:list){
            if (e instanceof NbtString s){
                entities.add(UUID.fromString(s.asString()));
            }
        }
        entityCount=entities.size();
    }

    protected void writeNbt(NbtCompound nbt) {
        nbt.putUuid(ID_KEY,id);
        nbt.putByte(PHASE_KEY,phase);
        nbt.putBoolean(STARTED_KEY,started);
        nbt.putInt(CENTER_X_KEY,center.getX());
        nbt.putInt(CENTER_Y_KEY,center.getY());
        nbt.putInt(CENTER_Z_KEY,center.getZ());
        nbt.putInt(TIME_KEY,time);
        nbt.putBoolean(IS_PHASE_INIT_KEY,isPhaseInit);
        NbtList list= new NbtList();
        for (UUID id: entities){
            list.add(NbtString.of(id.toString()));
        }
        nbt.put(ENTITY_LIST_KEY,list);
    }
    public void markDirty(){
        if (!world.isClient){
            RitualManager.get((ServerWorld) world).markDirty();
        }
    }
    public boolean shouldInitPhase(){
        if (!isPhaseInit){
            isPhaseInit =true;
            markDirty();
            return true;
        }
        return false;
    }
    protected void onEntityDeath(UUID id){
        NightOfTheDead.LOGGER.info("dead entity");
        if (entities.remove(id)){
            entityCount--;
        }
        markDirty();
    }

    /**returns whenever one entity or more linked to this ritual is alive*/
    public boolean areEntitiesAlive(){
        for (UUID id: entities){
            Entity entity= ((ServerWorld)world).getEntity(id);
            if (entity!=null&&entity.isAlive()){
                return true;
            }
        }
        return false;
    }
    protected void spawnEntity(MobEntity entity){
        entity.setPersistent();
        world.spawnEntity(entity);
        RitualManager.get((ServerWorld) world).entityMapping.put(entity.getUuid(),this.id);
        entities.add(entity.getUuid());
        entityCount++;
        markDirty();
    }
}
