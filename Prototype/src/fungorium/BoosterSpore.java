package fungorium;

/**
 * Represents a BoosterSpore in the Fungorium game.
 *
 * A BoosterSpore provides nutrients to an insect and temporarily
 * boosts its abilities for a number of rounds.
 */
public class BoosterSpore extends Spore {

    /**
     * Constructs a new BoosterSpore.
     *
     * @param owner the Fungus Farmer who owns the spore
     * @param nutrientContent the amount of nutrients the spore provides
     * @param effectDuration the number of rounds the boost effect lasts
     * @param name the name of the spore
     */
    public BoosterSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    /**
     * Handles the event when an insect eats this BoosterSpore.
     *
     * Applies a boost effect to the insect for the spore's effect duration,
     * and returns the nutrient content provided.
     *
     * @param i the insect that eats the spore
     * @return the amount of nutrients given to the insect
     */
    @Override
    public int gotEatenBy(Insect i) {
        i.setBoostedForRounds(effectDuration);
        return nutrientContent;
    }

    /**
     * Returns a string representation of the BoosterSpore.
     *
     * @return a string describing the BoosterSpore
     */
    @Override
    public String toString() {
        return sporeToString("booster spore");
    }
}