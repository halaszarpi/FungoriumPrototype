package fungorium;

/**
 * The OrdinaryTecton class represents a standard type of tecton in the game, inheriting from the
 * Tecton class and providing basic functionality for handling mycelium and tecton breaking logic.
 */
public class OrdinaryTecton extends Tecton {

    /**
     * Constructor to initialize an OrdinaryTecton with a given break probability, name, and map.
     *
     * @param tectonName The name of the tecton.
     * @param map The map that this tecton belongs to.
     */
    public OrdinaryTecton(String tectonName, TectonMap map){ super(tectonName, map); }

    /**
     * Adds mycelium to this tecton and logs the addition via the view.
     *
     * @param m The Mycelium to add to this tecton.
     * @throws Exception If the mycelium cannot be added.
     */
    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded(m);
    }

    /**
     * Handles the logic for the passage of a round. Determines if the tecton will break based on its break probability,
     * and performs the break if necessary.
     */
    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(null); }
    }

    /**
     * Determines if a body can be placed on this tecton. Since this is an ordinary tecton, the body can be placed.
     *
     * @return true if a body can be placed, false otherwise.
     */
    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }

    /**
     * Handles the tecton breaking logic. It creates a new tecton (of the same type), modifies the current tecton's name,
     * and adjusts connections accordingly.
     */
    @Override
    public void breakTecton(String oneNeighbourNameOfTecton) {

        Tecton randomTecton = generateRandomTectonNeighbour(oneNeighbourNameOfTecton);

        Tecton newTecton = new OrdinaryTecton( name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton, randomTecton);

        map.addTecton(newTecton);
    }

    /**
     * This method is called to "vanish" mycelium from this tecton. This operation is not allowed for this type of tecton.
     *
     * @throws Exception If this operation is attempted, an exception is thrown indicating it's not allowed for ordinary tectons.
     */
    @Override
    public void vanishMycelium() throws Exception {}

}