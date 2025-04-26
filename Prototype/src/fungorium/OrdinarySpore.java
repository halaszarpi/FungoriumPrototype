package fungorium;

public class OrdinarySpore extends Spore {
    private static final String type = "ordinary spore";

    public OrdinarySpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(type, owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        return nutrientContent;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}