package fungorium;
/**
 * The Mycelium class represents a mycelium entity owned by a FungusFarmer that can grow and spread on Tectons. 
 * It provides the functionality for the mycelium to interact with the world by growing bodies, spreading to other Tectons,
 * scattering spores, and interacting with insects. The Mycelium class implements the IRoundFollower interface, 
 * which allows it to take actions that follow the rounds of the game.
 */
import java.util.ArrayList;

public class Mycelium implements IRoundFollower{
    private final String name;
    private final FungusFarmer owner;
    private Tecton tecton;
    private FungusBody body;
    private int roundsToLive;
    private final MyceliumView view;
    private final int maxRoundsToLive = 3;
  /**
     * Constructs a new Mycelium instance.
     * 
     * @param name The name of the mycelium.
     * @param owner The FungusFarmer who owns the mycelium.
     * @param tecton The Tecton the mycelium is located on.
     */
    public Mycelium(String name, FungusFarmer owner, Tecton tecton) {
        this.name = name;
        this.owner = owner;
        this.tecton = tecton;
        this.body = null;
        this.roundsToLive = maxRoundsToLive;
        this.view = new MyceliumView(this);
    }

    // Getterek
    public String getName() {
        return name;
    }

    public FungusFarmer getOwner() {
        return owner;
    }

    public Tecton getTecton() {
        return tecton;
    }

    public FungusBody getBody() {
        return body;
    }

    /**
     * Attempts to grow a fungus body on the mycelium.
     * 
     * @param spore The spore used to grow the body.
     * @throws Exception If the mycelium already has a body, or if there are insufficient action points or spores on the tecton.
     */
    /*ha nincsen gombatest noveszt egyet es true ertekkel ter vissza, ha pedig mar van false-al ter vissza */
    public void growBody(Spore spore) throws Exception {
        if (body != null) {
            throw new Exception("This mycelium already has a body.");
        }
        if (owner.getActionPoints() < 1) {
            throw new Exception("Not enough action points to grow body.");
        }

        // Ha rakhatunk ra gombatestet es rajta van a noveszteshez hasznalando spora a tektonok akkor oke
        if(!tecton.hasSpores(spore))
            throw new Exception("No spores on the tecton to grow a body.");
        if (!tecton.canPlaceBody()) {
            throw new Exception("Cannot place body on this tecton.");
        }

        body = new FungusBody(this);
        tecton.removeSpore(spore);
        owner.removeSpore(spore);
        view.hasGrownBody();
        owner.useActionPoints(2);
        owner.increaseScore(1);
    }

    public void growBody(Spore spore, String testSpore) throws Exception {
        if (body != null) {
            throw new Exception("This mycelium already has a body.");
        }
        if (owner.getActionPoints() < 1) {
            throw new Exception("Not enough action points to grow body.");
        }

        // Ha rakhatunk ra gombatestet es rajta van a noveszteshez hasznalando spora a tektonok akkor oke
        if(!tecton.hasSpores(spore))
            throw new Exception("No spores on the tecton to grow a body.");
        if (!tecton.canPlaceBody()) {
            throw new Exception("Cannot place body on this tecton.");
        }

        body = new FungusBody(this, testSpore);
        tecton.removeSpore(spore);
        owner.removeSpore(spore);
        view.hasGrownBody();
        owner.useActionPoints(2);
        owner.increaseScore(1);
    }

    /**
     * Attempts to spread the mycelium to a neighboring tecton.
     * 
     * @param targetTecton The target tecton to spread to.
     * @throws Exception If the target tecton is not a valid neighbor or if a mycelium already exists on the target.
     */
    public void spreadTo(Tecton targetTecton) throws Exception {
        // Szomszedos tekton-e, ahova akar terjedni
        if (targetTecton == null || !tecton.isNeighbour(targetTecton)) {
            throw new Exception(view.invalidTarget(targetTecton));
        }

        // Van-e mar mycelium a tektonon
        if (targetTecton.hasMycelium(owner) && this.getTecton().isConnectedTo(targetTecton)) {
            throw new Exception(view.targetAlreadyHasMycelium(targetTecton));
        }

        if(targetTecton.hasMycelium(owner)){
            targetTecton.addConnection(tecton);
            owner.useActionPoints(2);
            view.connectionAddedTo(targetTecton);
        }
        else {
            Mycelium newMycelium = new Mycelium(owner.getNewMyceliumName(), owner, targetTecton);
            targetTecton.addMycelium(newMycelium);
            targetTecton.addConnection(tecton);
            owner.useActionPoints(2);
            owner.addMycelium(newMycelium);

            view.hasSpreadTo(targetTecton);
        }
    }

    /**
     * Handles the case where the fungus body of the mycelium dies.
     */
    public void bodyDied() {
        this.body = null;
        view.bodyHasDied();
    }
    /**
     * Attempts to scatter a spore to a target tecton.
     * 
     * @param targetTecton The target tecton to scatter the spore to.
     * @param sporeType The type of the spore to scatter.
     * @param sporeName The name of the spore to scatter.
     * @throws Exception If the mycelium does not have a fungus body, or if there is an error scattering the spore.
     */
    public void scatterSpore(Tecton targetTecton) throws Exception {
        if (body == null) {
            throw new Exception(view.hasNoFungusBody());
        }
        body.scatterTo(targetTecton);
        owner.useActionPoints(1);
    }
    /**
     * This method is called when a round has passed to reduce the cooldown of the mycelium's body.
     */
    public void roundPassed() {

        if (!tecton.findBody(owner, new ArrayList<>(), tecton)) {
            decreaseRoundsToLive();
        }

        if (body != null) {
            body.reduceCooldown();
            view.cooldownReduced();
        }
    }
    /**
     * Checks if the mycelium has a fungus body.
     * 
     * @return True if the mycelium has a body, false otherwise.
     */
    public boolean hasBody() {
        return body != null;
    }
    /**
     * Increases the rounds the mycelium can live if possible.
     */
    public void increaseRoundsToLive() {
        if (roundsToLive < 3) {
            roundsToLive++;
            view.myceliumSustained();
        }
    }
    /**
     * Decreases the rounds the mycelium can live and checks if it has died.
     */
    public void decreaseRoundsToLive() {
        if (roundsToLive > 0) { roundsToLive--; }
        if (roundsToLive == 0) {
            tecton.removeMycelium(this);
            view.myceliumHasDied();
        }
    }
    /**
     * Handles the action of the mycelium eating an insect.
     * 
     * @param insect The insect that is eaten.
     */
    public void eatInsect(Insect insect){
        insect.gotEaten();
        owner.useActionPoints(3);
        view.ateInsect(insect);
    }
    /**
     * Returns a string representation of the mycelium's state.
     * 
     * @return A string representing the mycelium's state.
     */
    public String toString() {
        String returnString = "\nMycelium name: " + this.name;
        returnString += "\n\tOwner: " + this.owner.getName();
        returnString += "\n\tTecton: " + this.tecton.getName();
        returnString += "\n\tHas Body: " + this.hasBody();
        returnString += "\n\tRounds to Live: " + this.roundsToLive;
        return returnString;
    }
    /**
     * Checks if two Mycelium objects are equal based on their tecton, name, and body state.
     * 
     * @param obj The object to compare to.
     * @return True if the mycelia are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        Mycelium m = (Mycelium)obj;
        return tecton.equals(m.tecton) && name.equals(m.name) && ((hasBody() && m.hasBody()) || (!hasBody() && !m.hasBody()));
    }
}