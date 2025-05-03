package fungorium;

public class OrdinarySpore extends Spore {

    /**
     * Constructs an OrdinarySpore with a given owner, nutrient content, effect duration, and name.
     *
     * @param owner The FungusFarmer who owns this spore.
     * @param nutrientContent The nutrient content of the spore.
     * @param effectDuration The duration for which the spore's effect lasts.
     * @param name The name of the spore.
     */
    public OrdinarySpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    /**
     * This method defines how much nutrient content the spore provides when eaten by an insect.
     *
     * @param i The insect that eats the spore.
     * @return The nutrient content of the spore, which is provided to the insect when it eats the spore.
     */
    @Override
    public int gotEatenBy(Insect i) {
        return nutrientContent;
    }

    /**
     * Returns a string representation of this spore as an "ordinary spore."
     *
     * @return A string description of the ordinary spore.
     */
    @Override
    public String toString() {
        return sporeToString("ordinary spore");
    }

}