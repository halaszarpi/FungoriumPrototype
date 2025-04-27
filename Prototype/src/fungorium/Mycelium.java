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
        this.view = new MyceliumView();
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
            view.hasGrownBody(this.name);
            owner.useActionPoints(1);
            return true;
        }
        return false;
    }

    public void spreadTo(Tecton targetTecton) throws Exception {
        // Szomszedos tekton-e, ahova akar terjedni
        try {
            if (targetTecton == null || !tecton.isNeighbour(targetTecton)) {
                return;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        // Ellenorzi, hogy van-e mar kapcsolat a ketto kozott
        try {
            if (tecton.isConnectedTo(targetTecton)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mycelium newMycelium = new Mycelium(name, owner, targetTecton);
        targetTecton.addMycelium(newMycelium);
        targetTecton.addConnection(tecton);
        owner.useActionPoints(2);

        view.hasSpreadTo(this.name, targetTecton.name);
    }


    public void bodyDied() {
        this.body = null;
        view.bodyHasDied(this.name);
    }

    public void scatterSpore(Tecton targetTecton) throws Exception {
        if (body == null) {
            // view hivas!
            throw new Exception(view.hasNoFungusBody(name));
        }
        body.scatterTo(targetTecton);
        owner.useActionPoints(1);
    }

    public void roundPassed() {
        if (body != null) {
            body.reduceCooldown();
            view.cooldownReduced(name);
        }
    }

    public boolean hasBody() {
        return body != null;
    }

    public void increaseRoundsToLive() {
        if (roundsToLive < 3) {
            roundsToLive++;
            view.myceliumSustained(name);    
        }
    }

    public void decreaseRoundsToLive() {
        if (roundsToLive > 0) { roundsToLive--; }
        if (roundsToLive == 0) {
            tecton.removeMycelium(this);
            view.myceliumHasDied(name);
        }
    }

    public void eatInsect(Insect insect){
        insect.gotEaten();
        owner.useActionPoints(3);
        view.ateInsect(name, insect.getName());
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