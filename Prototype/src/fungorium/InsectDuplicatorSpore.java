package fungorium;

public class InsectDuplicatorSpore extends Spore{

    private static final String type = "insect duplicator spore";

    public InsectDuplicatorSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(type, owner, nutrientContent, effectDuration, name);
    }

    @Override
    public int gotEatenBy(Insect i) {
        i.duplicate();
        return nutrientContent;
    }
    @Override
    public String toString() {
        return super.toString();
    }
}