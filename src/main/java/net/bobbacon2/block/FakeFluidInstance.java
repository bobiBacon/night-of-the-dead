package net.bobbacon2.block;

public class FakeFluidInstance {
    public final FakeFluid fluid;
    public float amount;

    public FakeFluidInstance(FakeFluid fluid,float amount) {
        this.fluid = fluid;
        this.amount=amount;
    }
    public FakeFluidInstance(FakeFluidInstance fakeFluidInstance) {
        this.fluid = fakeFluidInstance.fluid;
        this.amount=fakeFluidInstance.amount;
    }

    public static FakeFluidInstance combine(FakeFluidInstance f,FakeFluidInstance f2){
        if (f.fluid!=f2.fluid){
            return null;
        }
        return new FakeFluidInstance(f.fluid, f.amount+ f2.amount);
    }
    public static boolean canCombine(FakeFluidInstance f,FakeFluidInstance f2){
        return f.fluid==f2.fluid;
    }
}
