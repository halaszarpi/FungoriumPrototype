package fungorium;

import java.util.ArrayList;

public class SingleMyceliumTecton extends Tecton {

    /**
     * Constructor for SingleMyceliumTecton.
     *
     * @param precentToBreak The percentage chance for the tecton to break.
     * @param tectonName The name of the tecton.
     * @param map The map where the tecton exists.
     */
    public SingleMyceliumTecton(int precentToBreak, String tectonName, TectonMap map) { super(precentToBreak, tectonName, map); }

    /**
     * Adds a mycelium to the tecton.
     * Only allows one mycelium to be added. Throws an exception if mycelium is already present.
     *
     * @param m The mycelium to be added.
     * @throws Exception if there is already a mycelium present.
     */
    @Override
    public void addMycelium(Mycelium m) throws Exception{

        if (myceliumList.isEmpty()) { myceliumList.add(m); }
        else { throw new Exception(view.singleMyceliumTectonAlreadyHasMycelium()); }
    }

    /**
     * Executes the round logic for the tecton.
     * Checks if the tecton should break and handles the result.
     */
    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(null); }
    }

    /**
     * Checks if the tecton can place a body. Returns true if it can.
     *
     * @return true if the tecton can place a body, false otherwise.
     */
    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }

    /**
     * Breaks the tecton and creates a new tecton with a modified name.
     * Manages the breakage of connections and neighbors.
     */
    @Override
    public void breakTecton(String oneNeighbourNameOfTecton) {

        Tecton randomTecton = generateRandomTectonNeighbour(oneNeighbourNameOfTecton);

        Tecton newTecton = new SingleMyceliumTecton(breakPrecent, name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton, randomTecton);

        view.tectonBreaks(newTecton);

        map.add(newTecton);
    }

    /**
     * Throws an exception indicating that this tecton cannot vanish mycelium.
     *
     * @throws Exception indicating this tecton cannot vanish mycelium.
     */
    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }

    /**
     * Provides a string representation of the tecton.
     *
     * @return A string indicating this is a "single mycelium tecton".
     */
    @Override
    public String toString() {
        return tectonToString("single mycelium tecton");
    }
}