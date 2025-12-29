package net.bobbacon.item;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.registry.ModRegistries;
import net.bobbacon.spell.Spell;
import net.bobbacon.spell.SpellType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ScrollItem extends Item {
    private static final String SPELL_KEY = "spell";
    public final SpellType<? extends Spell> spellType;

    public ScrollItem(SpellType<? extends Spell> spellType, Settings settings) {
        super(settings);
        this.spellType= spellType;
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

    @Override
    public String getTranslationKey(ItemStack stack) {
        SpellType<?> spell = getSpell(stack);
        if (!spell.isEmpty()){
            return "item.night-of-the-dead.scroll.spell."+ModRegistries.SPELL_TYPES.getId(spell).getPath();
        }
        return super.getTranslationKey(stack);
    }
}
