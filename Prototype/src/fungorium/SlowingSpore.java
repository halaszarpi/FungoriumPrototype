package fungorium;

public class SlowingSpore extends Spore {

    /**
     * Constructor for SlowingSpore.
     *
     * @param owner The fungus farmer that owns the spore.
     * @param nutrientContent The amount of nutrients in the spore.
     * @param effectDuration The duration for which the insect is slowed.
     * @param name The name of the spore.
     */
    public SlowingSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    /**
     * Defines the behavior when the spore is eaten by an insect.
     * Slows the insect for the duration of the effect.
     *
     * @param i The insect that eats the spore.
     * @return The nutrient content of the spore.
     */
    @Override
    public int gotEatenBy(Insect i) {
        i.setSlowedForRounds(effectDuration);
        return nutrientContent;
    }

    /**
     * Provides a string representation of the SlowingSpore.
     *
     * @return A string indicating this is a "slowing spore".
     */
    @Override
    public String toString() {
        return sporeToString("slowing spore");
    }

}