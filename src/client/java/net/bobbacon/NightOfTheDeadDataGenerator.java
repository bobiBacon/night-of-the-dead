package net.bobbacon;

import net.bobbacon.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NightOfTheDeadDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(MyRecipeGenerator::new);
		pack.addProvider(MyTagGenerator::new);
		pack.addProvider(ModModelGenerator::new);
		pack.addProvider(ModEnglishLangProvider::new);

	}
	private static class MyTagGenerator extends FabricTagProvider<Item> {
		public static final TagKey<Item> STRONG_ALCOHOL = TagKey.of(RegistryKeys.ITEM, Identifier.of(NightOfTheDead.MOD_ID, "strong_alcohol"));

		public MyTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.ITEM, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(STRONG_ALCOHOL)
					.add(ModItems.VODKA)
					.add(ModItems.RUM)
					.add(ModItems.WHISKEY);
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
					.input(MyTagGenerator.STRONG_ALCOHOL).input(Items.BLAZE_POWDER,4)
					.criterion(FabricRecipeProvider.hasItem(Items.BLAZE_POWDER),
							FabricRecipeProvider.conditionsFromItem(Items.BLAZE_POWDER))
					.offerTo(consumer);
			ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.MOLOTOV).pattern("gp").pattern(" a")
					.input('g', Items.GLOWSTONE_DUST).input('p',Items.PAPER).input('a',ModItems.PURE_ALCOHOL)
					.criterion(FabricRecipeProvider.hasItem(ModItems.PURE_ALCOHOL),
							FabricRecipeProvider.conditionsFromItem(ModItems.PURE_ALCOHOL))
					.offerTo(consumer);
			ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BREWING_BARREL).pattern("psp").pattern("pcp").pattern("psp")
					.input('s',ItemTags.WOODEN_SLABS).input('p', ItemTags.PLANKS).input('c',Items.COPPER_INGOT)
					.criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT),
							FabricRecipeProvider.conditionsFromItem(Items.COPPER_INGOT))
					.offerTo(consumer);
		}


	}
	public static class ModModelGenerator extends FabricModelProvider {
		public ModModelGenerator(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			itemModelGenerator.register(ModItems.VODKA, Models.GENERATED);
			itemModelGenerator.register(ModItems.RUM, Models.GENERATED);
			itemModelGenerator.register(ModItems.BEER, Models.GENERATED);
			itemModelGenerator.register(ModItems.WHISKEY, Models.GENERATED);
			itemModelGenerator.register(ModItems.MEAD, Models.GENERATED);
			itemModelGenerator.register(ModItems.VINEGAR, Models.GENERATED);
			itemModelGenerator.register(ModItems.PURE_ALCOHOL, Models.GENERATED);



		}
	}
	private static class ModEnglishLangProvider extends FabricLanguageProvider {
		private ModEnglishLangProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataGenerator, "en_us");
		}
		@Override
		public void generateTranslations(TranslationBuilder translationBuilder) {
			translationBuilder.add(ModItems.WHISKEY,"Whiskey");
			translationBuilder.add(ModItems.VODKA,"Vodka");
			translationBuilder.add(ModItems.VINEGAR,"Vinegar");
			translationBuilder.add(ModItems.MOLOTOV,"Molotov Cocktail");
			translationBuilder.add(ModItems.FIERY_MOLOTOV,"Fiery Cocktail");
			translationBuilder.add(ModItems.BREWING_BARREL,"Brewing Barrel");
			translationBuilder.add(ModItems.RUM,"Rum");
			translationBuilder.add(ModItems.PURE_ALCOHOL,"Pure Alcohol");
			translationBuilder.add(ModItems.MEAD,"Mead");
			translationBuilder.add(ModItems.BEER,"Beer");
		}
	}

}
