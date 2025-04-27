package fungorium;

/**
 * Represents an AntiSeverSpore in the Fungorium game.
 *
 * An AntiSeverSpore provides nutrients to an insect and temporarily
 * prevents the insect from being severed for a number of rounds.
 */
public class AntiSeverSpore extends Spore {

    /**
     * Constructs a new AntiSeverSpore.
     *
     * @param owner the Fungus Farmer who owns the spore
     * @param nutrientContent the amount of nutrients the spore provides
     * @param effectDuration the number of rounds the anti-sever effect lasts
     * @param name the name of the spore
     */
    public AntiSeverSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    /**
     * Handles the event when an insect eats this AntiSeverSpore.
     *
     * Applies the anti-sever effect to the insect for the spore's effect duration,
     * and returns the nutrient content provided.
     *
     * @param i the insect that eats the spore
     * @return the amount of nutrients given to the insect
     */
    @Override
    public int gotEatenBy(Insect i) {
        i.setAntiSeveredForRounds(effectDuration);
        return nutrientContent;
    }

    /**
     * Returns a string representation of the AntiSeverSpore.
     *
     * @return a string describing the AntiSeverSpore
     */
    @Override
    public String toString() {
        return sporeToString("antisever spore");
    }

}