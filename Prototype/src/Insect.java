public class Insect implements IRoundFollower{

    private String name;
    private Tecton tecton;
    private InsectKeeper owner;
    private InsectView view;
    private int antiSeveredForRounds;
    private int stunnedForRounds;
    private int slowedForRounds;
    private int boostedForRounds;

    private boolean hasEnoughActionPointsForStepping() {
        if (slowedForRounds > 0 && owner.getActionPoints() >= 3) { return true; }
        else if (boostedForRounds > 0 && owner.getActionPoints() >= 1) { return true; }
        else if (boostedForRounds == 0 && slowedForRounds == 0 && owner.getActionPoints() >= 2) { return true; }
        else { return false; }
    }

    private int actionPointsForStepping() {
        if (slowedForRounds > 0) { return 3; }
        else if (boostedForRounds > 0) { return 1; }
        else { return 2; }
    }

    public Insect(String insectName, Tecton tecton, InsectKeeper owner, InsectView view) {
        this.name = insectName;
        this.tecton = tecton;
        this.owner = owner;
        this.view = view;
        antiSeveredForRounds = 0;
        stunnedForRounds = 0;
        slowedForRounds = 0;
        boostedForRounds = 0;
        view.insectInitialized(name, this.tecton.getName());
    }

    public int getAntiSeveredForRounds() { return this.antiSeveredForRounds; }
    public int getStunnedForRounds() { return this.stunnedForRounds; }
    public int getSlowedForRounds() { return this.slowedForRounds; }
    public int getBoostedForRounds() { return this.boostedForRounds; }

    public void setAntiSeveredForRounds(int numberOfRounds) {
        this.antiSeveredForRounds = numberOfRounds;
        view.insectIsAntiSevered(name, numberOfRounds);
    }

    public void setStunnedForRounds(int numberOfRounds) {
        this.stunnedForRounds = numberOfRounds;
        view.insectIsStunned(name, numberOfRounds);
    }

    public void setSlowedForRounds(int numberOfRounds) {
        this.slowedForRounds = numberOfRounds;
        view.insectIsSlowed(name, numberOfRounds);
    }

    public void setBoostedForRounds(int numberOfRounds) {
        this.boostedForRounds = numberOfRounds;
        view.insectIsBoosted(name, numberOfRounds);
    }

    // Spora megevese 1-be kerul (?)
    public int eatSpore(Spore spore) {
        if (!(owner.getActionPoints() >= 1) || stunnedForRounds > 0) {
            return -1;
        }
        int nutrientContent = spore.gotEatenBy(this);
        owner.useActionPoints(1);
        view.insectAteSpore(name, spore.getName());
        return nutrientContent;
    }

    public void gotEaten() {
        if (stunnedForRounds > 0) {
            owner.insectDied(this);
            view.insectGotEaten(name);
        }
    }

    public void duplicate() {
        owner.duplicateInsect(this);
        view.insectDuplicated(name);
    }

    // TODO: Milyen exception az, amit dob?
    public boolean stepToTecton(Tecton targetTecton) throws Exception {
        if (!hasEnoughActionPointsForStepping() || stunnedForRounds > 0) {
            throw new Exception();
            return false;
        }
        else if (this.tecton.isConnectedTo(targetTecton)) {
            this.tecton.removeInsect(this);
            targetTecton.addInsect(this);
            int actionPointsUsed = actionPointsForStepping();
            this.owner.useActionPoints(actionPointsUsed);
            view.insectSteppedToTecton(name, targetTecton.getName());
            return true;
        }
        else {
            throw new Exception();
            return false;
        }
    }

    // TODO: Milyen exception az, amit dob?
    public boolean cutMycelium(Tecton targetTecton) throws Exception {
        if (!hasEnoughActionPointsForStepping() || stunnedForRounds > 0 || antiSeveredForRounds > 0) {
            throw new Exception();
            return false;
        }
        else if (this.tecton.isConnectedTo(targetTecton)) {
            this.tecton.removeConnection(targetTecton);
            this.owner.useActionPoints(1);
            view.insectCutMycelium(name, this.tecton.getName(), targetTecton.getName());
            return true;
        }
        else {
            throw new Exception();
            return false;
        }
    }

    public String getName() { return this.name; }

    // Insect adatai: kiiras elott es utan is van egy uj sor direkt
    public String toString() {
        String returnString = "\nInsect name: ";
        returnString += this.name;
        returnString += "\nInsect's tecton: ";
        returnString += this.tecton.getName();
        returnString += "\nInsect's owner: ";
        returnString += this.owner.getName();
        returnString += "\nInsect's effects: (duration in rounds)\n";
        returnString += "\tanti-severed: " + antiSeveredForRounds + "\n";
        returnString += "\tstunned: " + stunnedForRounds + "\n";
        returnString += "\tslowed: " + slowedForRounds + "\n";
        returnString += "\tboosted: " + boostedForRounds + "\n\n";
        return returnString;
    }

    public void roundPassed() {
        if (antiSeveredForRounds > 0) { antiSeveredForRounds--; }
        if (stunnedForRounds > 0) { stunnedForRounds--; }
        if (slowedForRounds > 0) { stunnedForRounds--; }
        if (boostedForRounds > 0) { stunnedForRounds--; }
        view.insectEffectsReduced(name);
    }

}
