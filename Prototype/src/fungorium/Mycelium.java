package fungorium;

public class Mycelium implements IRoundFollower{
    private String name;
    private FungusFarmer owner;
    private Tecton tecton;
    private FungusBody body;
    private int roundsToLive;
    private MyceliumView view;
    private final int maxRoundsToLive = 3;

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

    public int getRoundsToLive() {
        return roundsToLive;
    }

    public void setRoundsToLive(int roundsToLive) {
        this.roundsToLive = roundsToLive;
    }


    /*ha nincsen gombatest noveszt egyet es true ertekkel ter vissza, ha pedig mar van false-al ter vissza */
    public boolean growBody(Spore spore) throws Exception {
        if (body != null) {
            throw new Exception("This mycelium already has a body.");
        }
        if (owner.getActionPoints() < 1) {
            throw new Exception("Not enough action points to grow body.");
        }

        // Ha rakhatunk ra gombatestet es rajta van a noveszteshez hasznalando spora a tektonok akkor oke
        if (tecton.hasSpores(spore) && tecton.canPlaceBody()) {
            body = new FungusBody(this);
            tecton.removeSpore(spore);
            owner.removeSpore(spore);
            view.hasGrownBody();
            owner.useActionPoints(1);
            owner.increaseScore(1);
            return true;
        }
        return false;
    }

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


    public void bodyDied() {
        this.body = null;
        view.bodyHasDied();
    }

    public void scatterSpore(Tecton targetTecton) throws Exception {
        if (body == null) {
            // view hivas!
            throw new Exception(view.hasNoFungusBody());
        }
        body.scatterTo(targetTecton);
        owner.useActionPoints(1);
    }

    public void roundPassed() {
        if (body != null) {
            body.reduceCooldown();
            view.cooldownReduced();
        }
    }

    public boolean hasBody() {
        return body != null;
    }

    public void increaseRoundsToLive() {
        if (roundsToLive < 3) {
            roundsToLive++;
            view.myceliumSustained();
        }
    }

    public void decreaseRoundsToLive() {
        if (roundsToLive > 0) { roundsToLive--; }
        if (roundsToLive == 0) {
            tecton.removeMycelium(this);
            view.myceliumHasDied();
        }
    }

    public void eatInsect(Insect insect){
        insect.gotEaten();
        owner.useActionPoints(3);
        view.ateInsect(insect);
    }

    public String toString() {
        String returnString = "\nMycelium name: " + this.name;
        returnString += "\n\tOwner: " + this.owner.getName();
        returnString += "\n\tTecton: " + this.tecton.getName();
        returnString += "\n\tHas Body: " + this.hasBody();
        returnString += "\n\tRounds to Live: " + this.roundsToLive;
        return returnString;
    }

    @Override
    public boolean equals(Object obj) {
        Mycelium m = (Mycelium)obj;
        return tecton.equals(m.tecton) && name.equals(m.name) && ((hasBody() && m.hasBody()) || (!hasBody() && !m.hasBody()));
    }
}