package net.bobbacon2;

import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.item.ModItems;
import net.bobbacon2.registry.ModRegistries;
import net.bobbacon2.render.entity.EntityRenderers;
import net.bobbacon2.render.fluids.ColoredWaterRenderHandler;
import net.bobbacon2.screen.ModScreenHandlers;
import net.bobbacon2.screen.RefiningScreen;
import net.bobbacon2.render.blockEntity.BlockEntityRenderers;
import net.bobbacon2.render.entity.EntityRenderers;
import net.bobbacon2.render.fluids.ColoredWaterRenderHandler;
import net.bobbacon2.screen.ModScreenHandlers;
import net.bobbacon2.screen.RefiningScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.Text;
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
		EntityRenderers.init();
		BlockEntityRenderers.init();
		HandledScreens.register(ModScreenHandlers.REFINERY_SCREEN_HANDLER, RefiningScreen::new);

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ETERNAL_FIRE, RenderLayer.getCutout());

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
		ModelPredicateProviderRegistry.register(
				ModItems.NAPALM_MOLOTOV,
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