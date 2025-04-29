package fungorium;

public class InsectDuplicatorSpore extends Spore {

    /**
     * Constructor for creating an InsectDuplicatorSpore.
     *
     * @param owner The owner (FungusFarmer) of the spore.
     * @param nutrientContent The nutrient content of the spore.
     * @param effectDuration The duration for which the spore's effect lasts.
     * @param name The name of the spore.
     */
    public InsectDuplicatorSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    /**
     * The effect of the InsectDuplicatorSpore when consumed by an insect.
     * The insect is duplicated when it eats the spore.
     *
     * @param i The insect that eats the spore.
     * @return The nutrient content of the spore.
     */
    @Override
    public int gotEatenBy(Insect i) {
        i.duplicate();
        return nutrientContent;
    }

    /**
     * Returns a string representation of the InsectDuplicatorSpore.
     *
     * @return A string describing the InsectDuplicatorSpore.
     */
    @Override
    public String toString() {
        return sporeToString("insect duplicator spore");
    }
}