package fungorium;

public class MyceliumView {
    
    public void hasGrownBody(String myceliumName) {
        System.out.println("Mycelium (" + myceliumName + ") has grown a FungusBody!");
    }

    public void hasSpreadTo(String myceliumName, String targetTectonName) {
        System.out.println("Mycelium (" + myceliumName + ") has spread to tecton (" + targetTectonName + ")!");
    }

    public void bodyHasDied(String myceliumName) {
        System.out.println("Mycelium's (" + myceliumName + ") FungusBody has died!");
    }

    public String hasNoFungusBody(String myceliumName) {
        return "\nMycelium (" + myceliumName + ") cannot scatter spore: no FungusBody present!\n";
    }

    public void cooldownReduced(String myceliumName) {
        System.out.println("Scattering cooldown reduced on mycelium's (" + myceliumName + ") FungusBody by 1!");
    }

    public void myceliumHasDied(String myceliumName) {
        System.out.println("Mycelium (" + myceliumName + ") has died!");
    }

    public void myceliumSustained(String myceliumName) {
        System.out.println("Mycelium's (" + myceliumName + ") life sustained!");
    }

    public void ateInsect(String myceliumName, String insectName) {
        System.out.println("Mycelium (" + myceliumName + ") ate insect (" + insectName + ")!");
    }

}