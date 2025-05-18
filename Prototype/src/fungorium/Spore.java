package fungorium;

public abstract class Spore {
    protected FungusFarmer owner;
    protected int nutrientContent;
    protected int effectDuration;
    protected String name;
    protected SporeView view;
    protected GSpore gs;

    /**
     * Constructor for the Spore class.
     *
     * @param owner The FungusFarmer that owns this spore.
     * @param nutrientContent The amount of nutrients the spore provides.
     * @param effectDuration How long the effect of the spore lasts (in rounds).
     * @param name The name of the spore.
     */
    protected Spore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        this.owner = owner;
        this.nutrientContent = nutrientContent;
        this.effectDuration = effectDuration;
        this.name = name;
        this.view = new SporeView(this);
        gs = new GSpore(this);
    }

    public SporeView getView() { return view;}

    /**
     * Abstract method to be implemented by subclasses of Spore.
     * Defines the behavior when a spore is eaten by an insect.
     *
     * @param i The insect that eats the spore.
     * @return The nutrient content of the spore (could be used to modify the state of the insect or player).
     */
    public abstract int gotEatenBy(Insect i);


    /**
     * Getter method for the name of the spore.
     *
     * @return The name of the spore.
     */
    public String getName() {
        return this.name;
    }

    public GSpore getGSpore() { 
        return gs;
    }

    public FungusFarmer getOwner() {
        return owner;
    }

}