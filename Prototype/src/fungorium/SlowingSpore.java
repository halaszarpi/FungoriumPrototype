package fungorium;

public class SlowingSpore extends Spore {

    public SlowingSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setSlowedForRounds(effectDuration);
        return nutrientContent;
    }

    @Override
    public String toString() {
        return sporeToString("slowing spore");
    }

}