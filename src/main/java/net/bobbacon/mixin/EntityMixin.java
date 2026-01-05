package net.bobbacon.mixin;

import net.bobbacon.Accessors.EntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements EntityAccessor {
    @Unique
    private static final String comesFromRitualKey= "comes_from_ritual";
    @Unique
    public boolean comesFromRitual= false;
    @Inject(method = "writeNbt", at=@At("TAIL"))
    private void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir){
        nbt.putBoolean(comesFromRitualKey,comesFromRitual);
    }
    @Inject(method = "readNbt", at=@At("TAIL"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci){
        comesFromRitual=nbt.getBoolean(comesFromRitualKey);
    }
    @Inject(method = "isFireImmune", at=@At("HEAD"), cancellable = true)
    private void fireImmune(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(night_of_the_Dead$comesFromRitual());
    }
    @Inject(method = "setOnFireFor", at = @At("HEAD"), cancellable = true)
    private void cancelFire(int seconds, CallbackInfo ci) {
        if (night_of_the_Dead$comesFromRitual()) {
            ci.cancel();
        }
    }
    @Override
    public boolean night_of_the_Dead$comesFromRitual() {
        return comesFromRitual;
    }

    @Override
    public void night_of_the_Dead$setComesFromRitual(boolean value) {
        comesFromRitual= value;
    }
}
