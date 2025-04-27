package fungorium;

public class InsectDuplicatorSpore extends Spore {

    public InsectDuplicatorSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.duplicate();
        return nutrientContent;
    }

    @Override
    public String toString() {
        return sporeToString("insect duplicator spore");
    }
}