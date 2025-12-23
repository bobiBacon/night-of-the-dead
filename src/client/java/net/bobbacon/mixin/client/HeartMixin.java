package net.bobbacon.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
// l'idée c'est de créer un nouveau type de coeur, puis d'aller mixin sur draw heart pour changer l'endroit de la ressource si c'est un tel coeur
//@Mixin(InGameHud.HeartType.class)
//public abstract class HeartMixin {
//    @Shadow
//    @Final
//    @Mutable
//    private static InGameHud.HeartType[] $VALUES;
//
//    private static final NoteBlockInstrument MUSIC_BOX = noteblockExpansion$addVariant("MUSIC_BOX", "music_box", NoteblockSounds.MUSIC_BOX);
//
//    @Invoker("<init>")
//    public static NoteBlockInstrument noteblockExpansion$invokeInit(String internalName, int internalId, String name, SoundEvent sound) {
//        throw new AssertionError();
//    }
//
//    private static NoteBlockInstrument noteblockExpansion$addVariant(String internalName, String name, SoundEvent sound) {
//        ArrayList<NoteBlockInstrument> variants = new ArrayList<NoteBlockInstrument>(Arrays.asList(NoteblockInstrumentMixin.$VALUES));
//        NoteBlockInstrument instrument = noteblockExpansion$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, name, sound);
//        variants.add(instrument);
//        NoteblockInstrumentMixin.$VALUES = variants.toArray(new NoteBlockInstrument[0]);
//        return instrument;
//    }
//}
