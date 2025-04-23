package fungorium;

public class SlowingSpore extends Spore {

    private static final String type = "slowing spore";

    public SlowingSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(type, owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setSlowedForRounds(effectDuration);
        return nutrientContent;
    }
    @Override
    public String toString() {
        return super.toString();
    }

}