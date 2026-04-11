package net.bobbacon2.entity.block_entity;

import net.bobbacon.TheSpellLibraryDataGenerator;
import net.bobbacon.ritual.Ritual;
import net.bobbacon2.NightOfTheDeadDataGenerator;
import net.bobbacon2.enchants.ModEnchantments;
import net.bobbacon2.ritual.CorruptionRitual;
import net.bobbacon.ritual.RitualManager;
import net.bobbacon2.item.ModItems;
import net.bobbacon2.ritual.CorruptionRitual;
import net.bobbacon2.spell.ItemAffectingRitual;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AltarBE extends BlockEntity {
//    private DefaultedList<ItemStack> items= DefaultedList.ofSize(1,ItemStack.EMPTY);
    ItemStack stack= ItemStack.EMPTY;
    protected static final UUID NULL_ID= UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String RITUAL_KEY = "ritual";
    public UUID ritualId= NULL_ID;

    public AltarBE(BlockPos pos, BlockState state) {
        super(ModBE.ALTAR_BE, pos, state);
    }

    public boolean tryCastRitual(ItemAffectingRitual itemAffectingRitual){
        if (itemAffectingRitual instanceof Ritual ritual && itemAffectingRitual.canAffect(getStack()) &&ritual.tryStart()){
            ritualId=ritual.id;
            markDirty();
            return true;
        }
        return false;
    }
    public boolean canCastRitual(Ritual ritual){
        return ritual.hasRitualSite();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        ritualId = nbt.getUuid(RITUAL_KEY);
//        Inventories.readNbt(nbt,items);
        stack=ItemStack.fromNbt(nbt.getCompound("Item"));

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
//        Inventories.writeNbt(nbt, this.items, true);
        nbt.putUuid(RITUAL_KEY,ritualId);
        nbt.put("Item", stack.writeNbt(new NbtCompound()));
    }
    public ItemStack getStack() {
//        return items.get(0);
        return stack;
    }
    public void setStack(ItemStack stack){
//        items.set(0,stack);
        this.stack=stack;
        markDirtyAndSync();
    }


    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()){
            markDirtyAndSync();

            return ActionResult.SUCCESS;
        }
        ItemStack playerStack= player.getStackInHand(hand);
        ItemStack stack= this.getStack();
        boolean b=false;
        if (playerStack.getItem() instanceof ToolItem toolItem){
            b= toolItem.getMaterial()== ToolMaterials.NETHERITE&& EnchantmentHelper.getLevel(ModEnchantments.BLOOD_STEAL,playerStack)>0;
        }
        if (stack.isEmpty()&&(playerStack.isIn(NightOfTheDeadDataGenerator.MyTagGenerator.ALTAR_PLACEABLE)||b)){
            this.setStack(playerStack.split(1));
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        } else if ((playerStack.isEmpty() || ItemStack.canCombine(playerStack, stack))) {
            if (hasRitual()){
                player.damage(world.getDamageSources().create(DamageTypes.MAGIC),3f);
                return ActionResult.SUCCESS;
            }
            player.giveItemStack(stack.copy());
            this.setStack(Items.AIR.getDefaultStack());
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    public void markDirtyAndSync() {
        markDirty();

        if (world != null && !world.isClient) {
//            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.getChunkManager().markForUpdate(pos);
            }
        }

    }

    public void removeRitual() {
        ritualId=NULL_ID;
        markDirty();
    }
    public boolean hasRitual(){
        return !ritualId.equals(NULL_ID);
    }
}
