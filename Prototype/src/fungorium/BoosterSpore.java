package fungorium;

public class BoosterSpore extends Spore {

    private static final String type = "booster spore";

    public BoosterSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(type, owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setBoostedForRounds(effectDuration);
        return nutrientContent;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}