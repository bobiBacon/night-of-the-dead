package net.bobbacon;

import net.bobbacon.item.ModItems;
import net.bobbacon.item.ScrollItem;
import net.bobbacon.registry.ModRegistries;
import net.bobbacon.render.blockEntity.BlockEntityRenderers;
import net.bobbacon.render.entity.EntityRenderers;
import net.bobbacon.render.fluids.ColoredWaterRenderHandler;
import net.bobbacon.render.item.ItemRenderers;
import net.bobbacon.spell.SpellType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class NightOfTheDeadClient implements ClientModInitializer {
	public static final String MOD_ID = "night-of-the-dead";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
//		MinecraftClient client = MinecraftClient.getInstance();

//		ModRegistries.SPELL_TYPES.stream().forEach(spell -> {
//			SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
//					.addSprite(spell.symbolTexture());
//		});
		ItemRenderers.init();
		EntityRenderers.init();
		BlockEntityRenderers.init();
		ModelPredicateProviderRegistry.register(
				ModItems.SCROLL,
				new Identifier("spell"),
				(stack, world, entity, seed) -> {
					SpellType<?> spell = ScrollItem.getSpell(stack);
					return spell == null ? 0 : spell.getNumericId();
				}
		);
		ModelPredicateProviderRegistry.register(
				ModItems.SCROLL,
				new Identifier("gui"),
				(stack, world, entity, seed) ->
						entity != null ? 1.0F : 0.0F
		);
		ModelPredicateProviderRegistry.register(
				ModItems.MOLOTOV,
				new Identifier("lit"),
				(stack, world, entity, seed) ->
						stack.getOrCreateNbt().getBoolean("lit") ? 1.0F : 0.0F
		);
		ModelPredicateProviderRegistry.register(
				ModItems.FIERY_MOLOTOV,
				new Identifier("lit"),
				(stack, world, entity, seed) ->
						stack.getOrCreateNbt().getBoolean("lit") ? 1.0F : 0.0F
		);

		ClientPlayNetworking.registerGlobalReceiver(
				NightOfTheDead.NIGHT_OF_THE_DEAD_PACKET,
				(client, handler, buf, responseSender) -> {
					boolean active = buf.readBoolean();
					client.execute(() -> {
						NightOfTheDeadClientManager.isNightOfTheDead = active;
						MinecraftClient.getInstance().worldRenderer.reload();
					});
				}
		);
//		ColorProviderRegistry.BLOCK.register(
//				(state, world, pos, tintIndex) -> {
//
//					if (world == null || pos == null) {
//						return -1;
//					}
//
//					if (NightOfTheDeadClientManager.isNightOfTheDead) {
//						return 0xFF3300; // eau rouge sang
//					}
//
//					return BiomeColors.getWaterColor(world, pos);
//				},
//				Blocks.WATER,
//				Blocks.BUBBLE_COLUMN
//		);
//		FluidRenderHandler original =
//				FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER);
//	LOGGER.warn(original.toString());
		FluidRenderHandlerRegistry.INSTANCE.register(
				Fluids.WATER,
				Fluids.FLOWING_WATER,
//				SimpleFluidRenderHandler.coloredWater(0xFF3300)
//				new FluidRenderHandler() {
//
//
//
//					@Override
//					public int getFluidColor(
//							BlockRenderView world,
//							BlockPos pos,
//							FluidState state
//					) {
//						LOGGER.warn("using custom handler");
//						if (NightOfTheDeadClientManager.isNightOfTheDead) {
//							return 0xFF3300; // rouge sang
//						}
//
//						return original.getFluidColor(world, pos, state);
//					}
//
//					@Override
//					public Sprite[] getFluidSprites(
//							@Nullable BlockRenderView blockRenderView, @Nullable BlockPos blockPos, FluidState fluidState
//					) {
//						return original.getFluidSprites(blockRenderView, blockPos, fluidState);
//					}
//				}
				new ColoredWaterRenderHandler(SimpleFluidRenderHandler.WATER_STILL,SimpleFluidRenderHandler.WATER_FLOWING,-1)
		);

	}
}