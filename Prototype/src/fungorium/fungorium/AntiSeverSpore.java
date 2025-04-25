package fungorium;

public class AntiSeverSpore extends Spore {

    private static final String type = "antisever spore";

    public AntiSeverSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(type, owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setAntiSeveredForRounds(effectDuration);
        return nutrientContent;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}