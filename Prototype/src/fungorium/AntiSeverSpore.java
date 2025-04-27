package fungorium;

public class AntiSeverSpore extends Spore {

    public AntiSeverSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setAntiSeveredForRounds(effectDuration);
        return nutrientContent;
    }

    @Override
    public String toString() {
        return sporeToString("antisever spore");
    }

}