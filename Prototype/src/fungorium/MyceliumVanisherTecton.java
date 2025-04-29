package fungorium;
/**
 * The MyceliumVanisherTecton class represents a type of Tecton where mycelium is actively "vanished"
 * by reducing its rounds to live in each round. This class manages mycelium life cycles, causing mycelium 
 * to lose its rounds and potentially die while also supporting tecton breaking mechanics.
 */
public class MyceliumVanisherTecton extends Tecton {
    /**
     * Constructs a new MyceliumVanisherTecton with the specified break percentage, name, and map.
     * 
     * @param precentToBreak The percentage chance that the tecton will break during the round.
     * @param tectonName The name of the tecton.
     * @param map The map the tecton belongs to.
     */
    public MyceliumVanisherTecton(int precentToBreak, String tectonName, TectonMap map) { super(precentToBreak, tectonName, map); }
    /**
     * Adds a Mycelium object to the list of mycelium on this tecton. 
     * The addition is followed by a notification through the view.
     * 
     * @param m The Mycelium instance to add.
     * @throws Exception If there is an error adding the mycelium.
     */
    @Override
    public void addMycelium(Mycelium m) throws Exception {
        myceliumList.add(m);

        view.myceliumAdded(m);
    }
    /**
     * This method is called at the end of each round. It performs the following actions:
     * - It vanishes mycelium by calling the `vanishMycelium()` method.
     * - It checks if the tecton breaks. If it does, it invokes `breakTecton()`.
     */
    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);
        vanishMycelium();
        if (tectonBreaks) { breakTecton(null); }
    }
    /**
     * This method checks if a body can be placed on this tecton by calling the helper method 
     * `canPlaceBodyHelper()` from the parent Tecton class.
     * 
     * @return true if a body can be placed, false otherwise.
     */
    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }
     /**
     * Simulates the tecton breaking, creating a new MyceliumVanisherTecton. 
     * The current tecton’s name is modified, and the new tecton is added to the map.
     */
    @Override
    public void breakTecton(String oneNeighbourNameOfTecton) {

        Tecton randomTecton = generateRandomTectonNeighbour(oneNeighbourNameOfTecton);

        Tecton newTecton = new MyceliumVanisherTecton(breakPrecent, name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton, randomTecton);

        view.tectonBreaks(newTecton);

        map.add(newTecton);
    }
     /**
     * Provides a string representation of the MyceliumVanisherTecton, 
     * indicating it is a "mycelium vanisher tecton".
     * 
     * @return A string representation of the tecton.
     */
    @Override
    public String toString() {
        return tectonToString("mycelium vanisher tecton");
    }
    /**
     * Vanishes all mycelium on this tecton by reducing their rounds to live. 
     * This action decreases the lifespan of each Mycelium in the list.
     */
    @Override
    public void vanishMycelium() {
        for (int i = myceliumList.size() - 1; i >= 0; i--) {
            Mycelium m = myceliumList.get(i);
            m.decreaseRoundsToLive();
        }
    }

}