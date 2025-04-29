package fungorium;

public class MyceliumView {

    Mycelium mycelium;

    public MyceliumView(Mycelium mycelium) {
        this.mycelium = mycelium;
    }

    public void hasGrownBody() {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium (" + mycelium.getName() + ") has grown a FungusBody!");
    }

    public void hasSpreadTo(Tecton targetTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium (" + mycelium.getName() + ") has spread to tecton (" + targetTecton.getName() + ")!");
    }

    public String invalidTarget(Tecton targetTecton) {
        return ("Tecton (" + targetTecton.getName() + "does not exist or not a neighbour to tecton (" + mycelium.getTecton().getName() + ")!");
    }

    public void bodyHasDied() {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium's (" + mycelium.getName() + ") FungusBody has died!");
    }

    public String hasNoFungusBody() {
        return "\nMycelium (" + mycelium.getName() + ") cannot scatter spore: no FungusBody present!\n";
    }

    public void cooldownReduced() {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Scattering cooldown reduced on mycelium's (" + mycelium.getName() + ") FungusBody by 1!");
    }

    public void myceliumHasDied() {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium (" + mycelium.getName() + ") has died!");
    }

    public void myceliumSustained() {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium's (" + mycelium.getName() + ") life sustained!");
    }

    public void ateInsect(Insect insect) {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium (" + mycelium.getName() + ") ate insect (" + insect.getName() + ")!");
    }

    public String targetAlreadyHasMycelium(Tecton targetTecton) {
        return ("Tecton (" + targetTecton.getName() + ") already has mycelium on it and there is a connection between them!");
    }

    public void connectionAddedTo(Tecton newTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Tecton (" + mycelium.getTecton().getName() + ") is reconnected to (" + newTecton.getName() + ")!");
    }
}