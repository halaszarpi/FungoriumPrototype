package fungorium;

public class StunningSpore extends Spore {

    private static final String type = "stunning spore";

    public StunningSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(type, owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.setStunnedForRounds(effectDuration);
        return nutrientContent;
    }
    @Override
    public String toString() {
        return super.toString();
    }
}