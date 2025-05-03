package fungorium;

public class NoBodyTecton extends Tecton {

    /**
     * Constructs a NoBodyTecton with a given break percentage, name, and associated map.
     *
     * @param precentToBreak The probability (as a percentage) that the tecton will break.
     * @param tectonName The name of the tecton.
     * @param map The map to which this tecton belongs.
     */
    public NoBodyTecton(int precentToBreak, String tectonName, TectonMap map) { super(precentToBreak, tectonName, map); }

    /**
     * Adds a mycelium to this tecton.
     *
     * @param m The mycelium to be added to the tecton.
     * @throws Exception If there is an error adding the mycelium.
     */
    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded( m);
    }

    /**
     * Simulates a round passing for this tecton.
     * The tecton has a chance to break based on the break percentage.
     * If it breaks, the tecton will be split into a new tecton.
     */
    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(null); }
    }

    /**
     * Indicates that no body can be placed on this tecton.
     *
     * @return false since this tecton does not allow a body to be placed.
     */
    @Override
    public boolean canPlaceBody() { return false; }

    /**
     * Breaks the tecton and creates a new tecton.
     * The original tecton will lose its name and break into a new tecton with a modified name.
     */
    @Override
    public void breakTecton(String oneNeighbourNameOfTecton) {

        Tecton randomTecton = generateRandomTectonNeighbour(oneNeighbourNameOfTecton);

        Tecton newTecton = new NoBodyTecton(breakPrecent, name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton, randomTecton);

        view.tectonBreaks(newTecton);

        map.add(newTecton);
    }

    /**
     * Throws an exception when attempting to vanish mycelium on this tecton.
     *
     * @throws Exception If mycelium cannot be vanished from this tecton.
     */
    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

    /**
     * Returns a string representation of this tecton.
     *
     * @return A string describing this tecton as a "no body tecton."
     */
    @Override
    public String toString() {
        return tectonToString("no body tecton");
    }

}