package fungorium;

public class Mycelium implements IRoundFollower{
    private Tecton tecton;
    private FungusBody fungusBody;
    public String name;
    private MyceliumView view;
    
    public Mycelium(Tecton tecton, String name, MyceliumView view){
        this.tecton = tecton;
        this.fungusBody = null;
        this.name=name;
        this.view = view;
    }

    //ha nincsen gombatest noveszt egyet es true ertekkel ter vissza, ha pedig mar van false-al ter vissza/
    public boolean growBody(FungusBodyView fungusBodyView) throws Exception {
        if (tecton.hasSpores() && tecton.canPlaceBody()) {
            // Itt majd a cooldown-t kicserelni
            fungusBody = new FungusBody(this, fungusBodyView);
            return true;
        }
        return false;
    }

    public void spreadTo(Tecton targetTecton, String name) throws Exception {
        //ellenorzi, hogy a tecton az nem null/
        /* ellenorzi a new tecton benne van-e a szomszedsagi listaban,/
        / es azt hogy van-e aktiv kapcsolat a ket tekton kozott */ 
        if (targetTecton != null && tecton.getNeighbours().containsKey(targetTecton)) {
            Mycelium newMycelium = new Mycelium(targetTecton , name, view);
            targetTecton.addConnection(this.tecton);
            targetTecton.addMycelium(newMycelium);
        }
    }
 
    public void bodyDied() {
        fungusBody = null;
    }

    public void scatterSpore(Tecton targetTecton, String sporeName) throws Exception {
        if (fungusBody != null) {
            fungusBody.scatterTo(targetTecton, sporeName);
        }
    }

    public void roundPassed() {
        if (fungusBody != null) {
            fungusBody.reduceCooldown();
        }
    }

    public boolean hasBody() {
        return fungusBody != null;
    }

    public Tecton getTecton(){
        return tecton;
    }

    @Override
    public String toString() {
        return "Mycelium name: " + name + "\nHasbody: " + (fungusBody != null);
    }

    // Teszteles erejeig felvesszuk, kesobb elhagyjuk
    public void setBody(FungusBody f){
        this.fungusBody = f;
    }

    public FungusFarmer getFarmer() { return new FungusFarmer(); }

    public String getName() { return name; }

    public void increaseRoundsToLive() {}

    public void eatInsect(Insect i) {}

    @Override
    public boolean equals(Object obj) {
        Mycelium m = (Mycelium)obj;
        return tecton.equals(m.tecton) && name.equals(m.name) && (fungusBody != null && m.fungusBody != null);
    }
}