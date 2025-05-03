package fungorium;
/**
 * The MyceliumSustainerTecton class is a type of Tecton that has special behavior to support mycelium growth 
 * and sustainability. It allows mycelium to live longer by increasing its rounds to live each round. 
 * Additionally, this type of tecton has the ability to break, creating new tectons as part of the game's mechanics.
 * It also manages mycelium added to it and does not support the vanishing of mycelium.
 */
public class MyceliumSustainerTecton extends Tecton{
    /**
     * Constructs a new MyceliumSustainerTecton with the specified break percentage, name, and map.
     * 
     * @param precentToBreak The percentage chance that the tecton will break during the round.
     * @param tectonName The name of the tecton.
     * @param map The map the tecton belongs to.
     */
    public MyceliumSustainerTecton(int precentToBreak, String tectonName, TectonMap map) { super(precentToBreak, tectonName, map); }
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
     * This method is called at the end of each round. It checks if the tecton breaks and, if so, 
     * creates new tectons and performs break-related actions. 
     * It also increases the rounds to live for all mycelium present on the tecton.
     */
    @Override
    public void roundPassed() {
        boolean tectonBreaks = generatedNumWithinBound(breakPrecent);

        if (tectonBreaks) { breakTecton(null); }
        for (Mycelium m : myceliumList){
            m.increaseRoundsToLive();
        }
    }
    /**
     * Checks if a body can be placed on this tecton. This method uses a helper function 
     * from the Tecton class to determine if placing a body is allowed.
     * 
     * @return true if a body can be placed, false otherwise.
     */
    @Override
    public boolean canPlaceBody() {
        return canPlaceBodyHelper();
    }
     /**
     * Simulates the tecton breaking and creates a new MyceliumSustainerTecton as a result. 
     * The current tecton’s name is modified, and the new tecton is added to the map.
     */
    @Override
    public void breakTecton(String oneNeighbourNameOfTecton) {

        Tecton randomTecton = generateRandomTectonNeighbour(oneNeighbourNameOfTecton);

        Tecton newTecton = new MyceliumSustainerTecton(breakPrecent, name + "-2", map);
        name += "-1";
        removeConnectionAtBreak();
        manageNeighboursAtBreak(newTecton, randomTecton);

        view.tectonBreaks(newTecton);

        map.add(newTecton);
    }
    /**
     * Throws an exception if an attempt is made to vanish mycelium from this type of tecton, 
     * as MyceliumSustainerTecton does not support the vanishing of mycelium.
     * 
     * @throws Exception with a message indicating that this tecton does not allow mycelium to vanish.
     */
    @Override
    public void vanishMycelium() throws Exception { throw new Exception(view.notMyceliumVanisherTecton()); }
    /**
     * Returns a string representation of this tecton with its type specified as "mycelium sustainer tecton".
     * 
     * @return A string representation of the tecton.
     */ 
    @Override
    public String toString() {
        return tectonToString("mycelium sustainer tecton");
    }

}