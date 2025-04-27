package fungorium;

public class OrdinarySpore extends Spore {

    public OrdinarySpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        return nutrientContent;
    }

    @Override
    public String toString() {
        return sporeToString("ordinary spore");
    }

}