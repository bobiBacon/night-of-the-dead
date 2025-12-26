package net.bobbacon.ritual;

import net.bobbacon.NightOfTheDead;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class RitualManager extends PersistentState {
    private static final String NAME = "ritual_manager";
    private static final String RITUALS_KEY = "rituals";
    private final HashMap<UUID, Ritual> rituals = new HashMap<>();
    private static final String TICK_KEY = "tick";
    public int currentTime=0;

    public World world;

    private RitualManager(ServerWorld world) {
        this.world =world;
    }
    public void tick(){

        for (Ritual ritual : this.rituals.values()) {
            ritual.tick();
        }
        if (currentTime%200==0){
            markDirty();
        }
    }
    public void add(Ritual ritual){
        rituals.put(ritual.id,ritual);
        markDirty();
    }
    public void remove(Ritual ritual){
        remove(ritual.id);
    }
    public void remove(UUID id){
        NightOfTheDead.LOGGER.info("removing id"+ id);
        Ritual ritual= rituals.get(id);
        if (ritual!=null){
            ritual.started=false;
        }
        rituals.remove(id);
        markDirty();
    }
    public static RitualManager fromNbt(ServerWorld world, NbtCompound nbt) {
        RitualManager ritualManager = new RitualManager(world);
        ritualManager.currentTime= nbt.getInt(TICK_KEY);
        NbtList nbtList = nbt.getList(RITUALS_KEY, NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Ritual ritual = new CorruptionRitual(world, nbtCompound);
            ritualManager.rituals.put(ritual.id, ritual);
        }

        return ritualManager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt(TICK_KEY, this.currentTime);
        NbtList nbtList = new NbtList();

        for (Ritual ritual : this.rituals.values()) {
            NbtCompound nbtCompound = new NbtCompound();
            ritual.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        nbt.put(RITUALS_KEY, nbtList);
        return nbt;
    }
    public static RitualManager get(ServerWorld world) {
        return world.getPersistentStateManager()
                .getOrCreate(nbt -> RitualManager.fromNbt(world, nbt), () -> new RitualManager(world),NAME);
    }


}
