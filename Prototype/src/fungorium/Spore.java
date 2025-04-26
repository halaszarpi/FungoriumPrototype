package fungorium;

public abstract class Spore {
    private String sporeType;
    protected FungusFarmer owner;
    protected int nutrientContent;
    protected int effectDuration;
    protected String name;
    
    protected Spore(String sporeType, FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        this.sporeType = sporeType;
        this.owner = owner;
        this.nutrientContent = nutrientContent;
        this.effectDuration = effectDuration;
        this.name = name;
    }

    public abstract int gotEatenBy(Insect i);

    public int getNutrientContent() {
        return this.nutrientContent;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        String returnString = "\tSpore name: ";
        returnString += this.name;
        returnString += "\n\tSpore type: ";
        returnString += this.sporeType;
        returnString += "\n\tSpore's nutrient content: ";
        returnString += this.nutrientContent;
        returnString += "\n\tSpore's effect duration: ";
        returnString += this.effectDuration;
        returnString += "\n\tSpore's owner: ";
        returnString += this.owner;
        return returnString;
    }

    @Override
    public boolean equals(Object obj) {
        Spore s = (Spore)obj;
        return sporeType.equals(s.sporeType) && owner.equals(s.owner) && nutrientContent == s.nutrientContent &&
                effectDuration == s.effectDuration && name.equals(s.name);
    }
}