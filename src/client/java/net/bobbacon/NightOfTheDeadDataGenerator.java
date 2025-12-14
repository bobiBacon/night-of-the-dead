package net.bobbacon;

import net.bobbacon.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NightOfTheDeadDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(MyRecipeGenerator::new);
		pack.addProvider(MyTagGenerator::new);
		pack.addProvider(ModModelGenerator::new);

	}
	private static class MyTagGenerator extends FabricTagProvider<Item> {
		public static final TagKey<Item> ALCOHOL = TagKey.of(RegistryKeys.ITEM, Identifier.of(NightOfTheDead.MOD_ID, "alcohol"));

		public MyTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.ITEM, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(ALCOHOL)
					.add(ModItems.VODKA);
		}

	}


	private static class MyRecipeGenerator extends FabricRecipeProvider {
		private MyRecipeGenerator(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> consumer) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.FIERY_MOLOTOV).pattern("bgb").pattern("tmt").pattern(" t ")
					.input('t', Items.GHAST_TEAR).input('b',Items.BLAZE_ROD).input('g',Items.GLOWSTONE_DUST).input('m',ModItems.MOLOTOV)
					.criterion(FabricRecipeProvider.hasItem(Items.GHAST_TEAR),
							FabricRecipeProvider.conditionsFromItem(Items.GHAST_TEAR))
					.offerTo(consumer);
			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PURE_ALCOHOL)
					.input(MyTagGenerator.ALCOHOL).input(Items.BLAZE_POWDER,4)
					.criterion(FabricRecipeProvider.hasItem(Items.BLAZE_POWDER),
							FabricRecipeProvider.conditionsFromItem(Items.BLAZE_POWDER))
					.offerTo(consumer);
			ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MOLOTOV).pattern("gp").pattern(" a")
					.input('g', Items.GLOWSTONE_DUST).input('p',Items.PAPER).input('a',ModItems.PURE_ALCOHOL)
					.criterion(FabricRecipeProvider.hasItem(ModItems.PURE_ALCOHOL),
							FabricRecipeProvider.conditionsFromItem(ModItems.PURE_ALCOHOL))
					.offerTo(consumer);
		}


	}
	public static class ModModelGenerator extends FabricModelProvider {
		public static final Model BREWING_BARREL = new Model(
				Optional.of(Identifier.of(NightOfTheDead.MOD_ID, "block/brewing")),
				Optional.empty());
		public ModModelGenerator(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//			BlockStateModelGenerator.
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {

		}
	}

}
