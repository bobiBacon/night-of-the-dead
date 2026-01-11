package net.bobbacon.item;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.registry.ModRegistries;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ScrollItem extends Item {
    private static final String SPELL_KEY = "spell";
    private static final String DECRYPTED_KEY = "decrypted";
    private static final String PLAYERS_KEY = "players";
    public ScrollItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NightOfTheDead.LOGGER.info("using");
        ItemStack stack= user.getStackInHand(hand);
        Spell spell= getSpell(stack).create(world);
        user.setCurrentHand(hand);
        return spell.canCast(user.getBlockPos(),world)? TypedActionResult.consume(stack):TypedActionResult.pass(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        NightOfTheDead.LOGGER.info("finish using");

        Spell spell= getSpell(stack).create(world);
        if (spell.tryCast(user.getBlockPos(),world)&&spell.isSingleUse()){
            stack.decrement(1);
        }
        return super.finishUsing(stack, world, user);
    }



    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks==0){
            finishUsing(stack,world,user);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    public static SpellType<?> getSpell(ItemStack stack){
        if (!stack.hasNbt()) return SpellType.EMPTY;

        NbtCompound nbt = stack.getNbt();
        if (!nbt.contains(SPELL_KEY, NbtElement.STRING_TYPE)) return SpellType.EMPTY;

        Identifier id = Identifier.tryParse(nbt.getString(SPELL_KEY));
        if (id == null) return  SpellType.EMPTY;

        return ModRegistries.SPELL_TYPES.get(id);
    }
    public static void setSpell(ItemStack stack, SpellType<?> spell) {
        Identifier id= ModRegistries.SPELL_TYPES.getId(spell);
        if (id==null){
            stack.getOrCreateNbt().putString(SPELL_KEY, "empty");
        }else {
            stack.getOrCreateNbt().putString(SPELL_KEY, id.toString());
        }
    }
    public static boolean canRead(PlayerEntity player, ItemStack stack){
        if (player.isCreative()||getSpell(stack).isEmpty()){
            return true;
        }
        for (NbtElement nbt:stack.getOrCreateNbt().getList(PLAYERS_KEY,NbtElement.STRING_TYPE)){
            NbtString nbt2= (NbtString) nbt;
            UUID uuid= UUID.fromString(nbt2.asString());
            if (player.getUuid()==uuid){
                return true;
            }
        }
        return false;
    }


    public static void decrypt(PlayerEntity player,ItemStack stack){
        stack.getOrCreateNbt().getList(PLAYERS_KEY,NbtElement.STRING_TYPE).addElement(0,NbtString.of(player.getUuid().toString()));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        SpellType<?> spell = getSpell(stack);
        if (spell.isEmpty()){
             return "item.night-of-the-dead.scroll.blank";
         }
        return super.getTranslationKey(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 80;
    }
}
