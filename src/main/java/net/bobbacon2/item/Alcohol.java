package net.bobbacon2.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
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
    private static final int MAX_USE_TIME = 32;
    public ArrayList<StatusEffectInstance> effects= new ArrayList<>();

    public Alcohol(Settings settings, StatusEffectInstance... effects) {
        super(settings.maxDamage(4).recipeRemainder(Items.GLASS_BOTTLE));
        this.effects= new ArrayList<>(List.of(effects));
    }
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            for (StatusEffectInstance statusEffectInstance : ((Alcohol)stack.getItem()).effects) {
                if (statusEffectInstance.getEffectType().isInstant()) {
                    statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0);
                } else {
                    user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                }
            }
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
    public static ItemStack decrement(ItemStack stack,ServerPlayerEntity player){
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
