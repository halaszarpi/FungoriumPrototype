package fungorium;

/**
 * Represents an insect in the game, capable of performing various actions such as stepping to a new tecton,
 * eating spores, and duplicating. It is also affected by certain status conditions like being stunned or slowed.
 * The insect implements the {@link IRoundFollower} interface, which allows it to perform actions when a round has passed.
 */
public class Insect implements IRoundFollower {

    private final String name;
    private Tecton tecton;
    private final InsectKeeper owner;
    private final InsectView view;
    private int antiSeveredForRounds;
    private int stunnedForRounds;
    private int slowedForRounds;
    private int boostedForRounds;

    /**
     * Constructor for creating a new insect.
     *
     * @param insectName The name of the insect.
     * @param tecton The tecton the insect starts in.
     * @param owner The owner of the insect.
     */
    public Insect(String insectName, Tecton tecton, InsectKeeper owner) {
        this.name = insectName;
        this.tecton = tecton;
        this.owner = owner;
        this.view = new InsectView(this);
        antiSeveredForRounds = 0;
        stunnedForRounds = 0;
        slowedForRounds = 0;
        boostedForRounds = 0;
        view.insectInitialized();
    }

    /**
     * Checks if the insect has enough action points to step to a new tecton.
     *
     * @return {@code true} if the insect can step, {@code false} otherwise.
     */
    private boolean hasEnoughActionPointsForStepping() {
        if (slowedForRounds > 0 && owner.getActionPoints() >= 3) { return true; }
        else if (boostedForRounds > 0 && owner.getActionPoints() >= 1) { return true; }
        else if (boostedForRounds == 0 && slowedForRounds == 0 && owner.getActionPoints() >= 2) { return true; }
        else { return false; }
    }

    /**
     * Returns the number of action points required for stepping to a new tecton.
     *
     * @return The number of action points required for stepping.
     */
    private int actionPointsForStepping() {
        if (slowedForRounds > 0) { return 3; }
        else if (boostedForRounds > 0) { return 1; }
        else { return 2; }
    }

    // Getter methods for insect's conditions
    public int getAntiSeveredForRounds() { return this.antiSeveredForRounds; }
    public int getStunnedForRounds() { return this.stunnedForRounds; }
    public int getSlowedForRounds() { return this.slowedForRounds; }
    public int getBoostedForRounds() { return this.boostedForRounds; }

    // Setter methods for insect's conditions with views
    public void setAntiSeveredForRounds(int numberOfRounds) {
        this.antiSeveredForRounds = numberOfRounds;
        view.insectIsAntiSevered();
    }

    public void setStunnedForRounds(int numberOfRounds) {
        this.stunnedForRounds = numberOfRounds;
        view.insectIsStunned();
    }

    public void setSlowedForRounds(int numberOfRounds) {
        this.slowedForRounds = numberOfRounds;
        view.insectIsSlowed();
    }

    public void setBoostedForRounds(int numberOfRounds) {
        this.boostedForRounds = numberOfRounds;
        view.insectIsBoosted();
    }

    /**
     * Instructs the insect to eat a spore.
     *
     * @param spore The spore to be eaten by the insect.
     * @return The nutrient content of the spore, or -1 if the insect can't eat it.
     */
    public int eatSpore(Spore spore) {
        if (!(owner.getActionPoints() >= 1) || stunnedForRounds > 0) {
            return -1;
        }
        int nutrientContent = spore.gotEatenBy(this);
        view.insectAteSpore(spore);
        tecton.removeSpore(spore);
        owner.useActionPoints(1);
        owner.increaseScore(nutrientContent);
        return nutrientContent;
    }

    /**
     * Handles the case where the insect is eaten.
     * If the insect is stunned, it dies.
     */
    public void gotEaten() {
        if (stunnedForRounds > 0) {
            owner.insectDied(this);
            view.insectGotEaten();
        }
    }

    /**
     * Duplicates the insect and notifies the owner.
     */
    public void duplicate() {
        owner.duplicateInsect(this);
        view.insectDuplicated();
    }

    /**
     * Attempts to move the insect to a new tecton.
     *
     * @param targetTecton The target tecton to step to.
     * @return {@code true} if the step was successful, {@code false} otherwise.
     * @throws Exception If the insect cannot step to the target tecton.
     */
    public boolean stepToTecton(Tecton targetTecton) throws Exception {
        if (!hasEnoughActionPointsForStepping() || stunnedForRounds > 0) {
            throw new Exception(view.insectNotAbleToStep(this.name, targetTecton.getName()));
        }
        else if (this.tecton.isConnectedTo(targetTecton)) {
            this.tecton.removeInsect(this);
            targetTecton.addInsect(this);
            this.tecton = targetTecton;
            int actionPointsUsed = actionPointsForStepping();
            this.owner.useActionPoints(actionPointsUsed);
            view.insectSteppedToTecton();
            return true;
        }
        else {
            throw new Exception(view.insectNotAbleToStep(this.name, targetTecton.getName()));
        }
    }

    /**
     * Attempts to cut the mycelium connection between tectons.
     *
     * @param targetTecton The target tecton to sever the connection to.
     * @return {@code true} if the connection was cut successfully, {@code false} otherwise.
     * @throws Exception If the insect cannot cut the mycelium connection.
     */
    public boolean cutMycelium(Tecton targetTecton) throws Exception {
        if (!hasEnoughActionPointsForStepping() || stunnedForRounds > 0 || antiSeveredForRounds > 0) {
            throw new Exception();
        }
        else if (this.tecton.isConnectedTo(targetTecton)) {
            this.tecton.removeConnection(targetTecton);
            this.owner.useActionPoints(1);
            view.insectCutMycelium(targetTecton);
            return true;
        }
        else {
            throw new Exception();
        }
    }

    // Getter methods for insect's details
    public String getName() { return this.name; }

    public Tecton getTecton() { return this.tecton; }

    public InsectKeeper getOwner() { return this.owner; }

    /**
     * Provides a string representation of the insect's state.
     *
     * @return A string representing the insect's state, including its name, tecton, owner, and status effects.
     */
    public String toString() {
        String returnString = "\n\tInsect name: ";
        returnString += this.name;
        returnString += "\n\tInsect's tecton: ";
        returnString += this.tecton.getName();
        returnString += "\n\tInsect's owner: ";
        returnString += this.owner.getName();
        returnString += "\n\tInsect's effects: (duration in rounds)\n";
        returnString += "\t\tanti-severed: " + antiSeveredForRounds + "\n";
        returnString += "\t\tstunned: " + stunnedForRounds + "\n";
        returnString += "\t\tslowed: " + slowedForRounds + "\n";
        returnString += "\t\tboosted: " + boostedForRounds + "\n\n";
        return returnString;
    }

    /**
     * Updates the insect's status effects at the end of each round.
     * This method reduces the duration of the insect's status effects.
     */
    public void roundPassed() {
        if (antiSeveredForRounds > 0) { antiSeveredForRounds--; }
        if (stunnedForRounds > 0) { stunnedForRounds--; }
        if (slowedForRounds > 0) { slowedForRounds--; }
        if (boostedForRounds > 0) { boostedForRounds--; }
        view.insectEffectsReduced();
    }

    /**
     * Compares this insect to another object for equality.
     *
     * @param obj The object to compare with.
     * @return {@code true} if the insects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        Insect i = (Insect)obj;
        return name.equals(i.name) && tecton.equals(i.tecton) && owner.equals(i.owner) && antiSeveredForRounds == i.antiSeveredForRounds &&
                stunnedForRounds == i.stunnedForRounds && slowedForRounds == i.slowedForRounds && boostedForRounds == i.boostedForRounds;
    }
}