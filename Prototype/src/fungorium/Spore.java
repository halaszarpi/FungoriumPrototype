package fungorium;

public abstract class Spore {
    protected FungusFarmer owner;
    protected int nutrientContent;
    protected int effectDuration;
    protected String name;

    protected Spore(FungusFarmer owner, int nutrientContent, int effectDuration, String name) {
        this.owner = owner;
        this.nutrientContent = nutrientContent;
        this.effectDuration = effectDuration;
        this.name = name;
    }

    public abstract int gotEatenBy(Insect i);

    public String getName() {
        return this.name;
    }

    public String sporeToString(String sporeType) {
        String returnString = "\tSpore name: ";
        returnString += this.name;
        returnString += "\n\tSpore type: ";
        returnString += sporeType;
        returnString += "\n\tSpore's nutrient content: ";
        returnString += this.nutrientContent;
        returnString += "\n\tSpore's effect duration: ";
        returnString += this.effectDuration;
        returnString += "\n\tSpore's owner: ";
        returnString += this.owner;
        return returnString;
    }
}