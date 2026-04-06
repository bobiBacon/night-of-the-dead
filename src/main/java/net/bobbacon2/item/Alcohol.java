package net.bobbacon2.item;

import net.bobbacon2.status_effect.ModEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Alcohol extends Item {
    protected static final int MAX_USE_TIME = 32;
    protected final boolean single_use;
    public ArrayList<StatusEffectInstance> effects= new ArrayList<>();

    public Alcohol(Settings settings,int usages, StatusEffectInstance... effects) {
        super(usages==1?settings.recipeRemainder(Items.GLASS_BOTTLE):settings.maxDamage(usages).recipeRemainder(Items.GLASS_BOTTLE));
        single_use=usages==1;
        this.effects= new ArrayList<>(List.of(effects));
    }
    public Alcohol(Settings settings, StatusEffectInstance... effects) {
       this(settings,1,effects);
    }
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            applyEffects(((Alcohol)stack.getItem()).effects,user);
            if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                if (!playerEntity.getAbilities().creativeMode) {
                    stack=decrement(stack,serverPlayerEntity);
                }
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }
    protected void applyEffects(List<StatusEffectInstance> effects,LivingEntity user){
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        for (StatusEffectInstance statusEffectInstance : effects) {
            if (statusEffectInstance.getEffectType().getCategory()== StatusEffectCategory.HARMFUL&&user.hasStatusEffect(ModEffects.SOBRIETY)){
                continue;
            }
            if (statusEffectInstance.getEffectType().isInstant()) {
                statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0);
            } else {
                user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }
    }
    public ItemStack decrement(ItemStack stack,ServerPlayerEntity player){
        if (single_use){
            player.giveItemStack(stack.copy().split(1).getRecipeRemainder());
            stack.decrement(1);
            return stack;
        }
        if (stack.damage(1, Random.create(),player)){
            return stack.getRecipeRemainder();
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
    @Nullable
    public static Alcohol getAlcoholType(ItemStack item){
        if (item.isOf(Items.POTATO)){
            return (Alcohol) ModItems.VODKA;
        }
        return null;
    }
}
