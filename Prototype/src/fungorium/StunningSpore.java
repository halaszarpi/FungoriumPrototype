package fungorium;

public class StunningSpore extends Spore {

    public StunningSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setStunnedForRounds(effectDuration);
        return nutrientContent;
    }

    @Override
    public String toString() {
        return sporeToString("stunning spore");
    }
}