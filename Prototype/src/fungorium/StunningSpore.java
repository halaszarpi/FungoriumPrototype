package fungorium;

public class StunningSpore extends Spore {

    /**
     * Constructor for StunningSpore.
     *
     * @param owner The FungusFarmer who owns the StunningSpore.
     * @param nutrientContent The nutrient content of the StunningSpore.
     * @param effectDuration The number of rounds the insect will be stunned.
     * @param name The name of the StunningSpore.
     */
    public StunningSpore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        super(owner, nutrientContent, effectDuration, name);
    }

    /**
     * When the spore is eaten by an insect, it stuns the insect for a specified number of rounds.
     *
     * @param i The insect that eats the spore.
     * @return The nutrient content of the spore.
     */
    @Override
    public int gotEatenBy(Insect i) {
        i.setStunnedForRounds(effectDuration);
        return nutrientContent;
    }

    /**
     * Provides a string representation of the StunningSpore.
     *
     * @return A string containing detailed information about the StunningSpore.
     */
    @Override
    public String toString() {
        return sporeToString("stunning spore");
    }
}