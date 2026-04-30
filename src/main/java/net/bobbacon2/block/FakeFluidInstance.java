package net.bobbacon2.block;

public class FakeFluidInstance {
    public FakeFluid fluid;
    public float amount;

    public FakeFluidInstance(FakeFluid fluid,float amount) {
        this.fluid = fluid;
        this.amount=amount;
    }
    public FakeFluidInstance(FakeFluidInstance fakeFluidInstance) {
        this.fluid = fakeFluidInstance.fluid;
        this.amount=fakeFluidInstance.amount;
    }
    public boolean isEmpty(){
        return fluid==FakeFluids.EMPTY||amount<=0;
    }
    public void transform(FakeFluid newFluid){
        fluid=newFluid;
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
