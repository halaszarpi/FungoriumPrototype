package fungorium;

public class BoosterSpore extends Spore {

    public BoosterSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setBoostedForRounds(effectDuration);
        return nutrientContent;
    }

    @Override
    public String toString() {
        return sporeToString("booster spore");
    }
}