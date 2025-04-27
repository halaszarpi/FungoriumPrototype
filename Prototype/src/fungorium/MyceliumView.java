package fungorium;

public class MyceliumView {

    Mycelium mycelium;

    public MyceliumView(Mycelium mycelium) {
        this.mycelium = mycelium;
    }

    public void hasGrownBody() {
        System.out.println("Mycelium (" + mycelium.getName() + ") has grown a FungusBody!");
    }

    public void hasSpreadTo(Tecton targetTecton) {
        System.out.println("Mycelium (" + mycelium.getName() + ") has spread to tecton (" + targetTecton.getName() + ")!");
    }

    public void bodyHasDied() {
        System.out.println("Mycelium's (" + mycelium.getName() + ") FungusBody has died!");
    }

    public String hasNoFungusBody() {
        return "\nMycelium (" + mycelium.getName() + ") cannot scatter spore: no FungusBody present!\n";
    }

    public void cooldownReduced() {
        System.out.println("Scattering cooldown reduced on mycelium's (" + mycelium.getName() + ") FungusBody by 1!");
    }

    public void myceliumHasDied() {
        System.out.println("Mycelium (" + mycelium.getName() + ") has died!");
    }

    public void myceliumSustained() {
        System.out.println("Mycelium's (" + mycelium.getName() + ") life sustained!");
    }

    public void ateInsect(Insect insect) {
        System.out.println("Mycelium (" + mycelium.getName() + ") ate insect (" + insect.getName() + ")!");
    }

}