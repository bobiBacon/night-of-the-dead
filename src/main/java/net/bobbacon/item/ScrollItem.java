package net.bobbacon.item;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.registry.ModRegistries;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellType;
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

import java.util.UUID;

public class ScrollItem extends Item {
    private static final String SPELL_KEY = "spell";
    private static final String PLAYERS_KEY = "players";
    private static final String UUID_KEY = "player_uuid";
    public ScrollItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NightOfTheDead.LOGGER.info("using");
        Spell spell= getSpell(user.getStackInHand(hand)).create(world);
        spell.tryCast(user.getBlockPos(),world);
        return super.use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        NightOfTheDead.LOGGER.info("finish using");

        Spell spell= getSpell(stack).create(world);
        spell.tryCast(user.getBlockPos(),world);
        return super.finishUsing(stack, world, user);
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
        for (NbtElement nbt:stack.getOrCreateNbt().getList(PLAYERS_KEY,NbtElement.STRING_TYPE)){
            NbtString nbt2= (NbtString) nbt;
            UUID uuid= UUID.fromString(nbt2.asString());
            if (player.getUuid()==uuid){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        SpellType<?> spell = getSpell(stack);
        if (!spell.isEmpty()){
            return "item.night-of-the-dead.scroll.spell."+ModRegistries.SPELL_TYPES.getId(spell).getPath();
        }
        return super.getTranslationKey(stack);
    }
}
