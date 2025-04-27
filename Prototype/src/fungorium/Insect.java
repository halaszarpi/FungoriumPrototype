package fungorium;

public class Insect implements IRoundFollower{

    private final String name;
    private Tecton tecton;
    private final InsectKeeper owner;
    private final InsectView view;
    private int antiSeveredForRounds;
    private int stunnedForRounds;
    private int slowedForRounds;
    private int boostedForRounds;

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

    public int getAntiSeveredForRounds() { return this.antiSeveredForRounds; }
    public int getStunnedForRounds() { return this.stunnedForRounds; }
    public int getSlowedForRounds() { return this.slowedForRounds; }
    public int getBoostedForRounds() { return this.boostedForRounds; }

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

    // Spora megevese 1-be kerul (?)
    public int eatSpore(Spore spore) {
        if (!(owner.getActionPoints() >= 1) || stunnedForRounds > 0) {
            return -1;
        }
        int nutrientContent = spore.gotEatenBy(this);
        view.insectAteSpore(spore);
        tecton.removeSpore(spore);
        owner.useActionPoints(1);
        return nutrientContent;
    }

    public void gotEaten() {
        if (stunnedForRounds > 0) {
            owner.insectDied(this);
            view.insectGotEaten();
        }
    }

    public void duplicate() {
        owner.duplicateInsect(this);
        view.insectDuplicated();
    }

    // TODO: Milyen exception az, amit dob?
    public boolean stepToTecton(Tecton targetTecton) throws Exception {
        if (!hasEnoughActionPointsForStepping() || stunnedForRounds > 0) {
            throw new Exception();
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
            throw new Exception();
        }
    }

    // TODO: Milyen exception az, amit dob?
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

    public String getName() { return this.name; }

    public Tecton getTecton() { return this.tecton; }

    public InsectKeeper getOwner() { return this.owner; }

    // Insect adatai: kiiras elott es utan is van egy uj sor direkt
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

    public void roundPassed() {
        if (antiSeveredForRounds > 0) { antiSeveredForRounds--; }
        if (stunnedForRounds > 0) { stunnedForRounds--; }
        if (slowedForRounds > 0) { stunnedForRounds--; }
        if (boostedForRounds > 0) { stunnedForRounds--; }
        view.insectEffectsReduced();
    }

    @Override
    public boolean equals(Object obj) {
        Insect i = (Insect)obj;
        return name.equals(i.name) && tecton.equals(i.tecton) && owner.equals(i.owner) && antiSeveredForRounds == i.antiSeveredForRounds &&
                stunnedForRounds == i.stunnedForRounds && slowedForRounds == i.slowedForRounds && boostedForRounds == i.boostedForRounds;
    }

}