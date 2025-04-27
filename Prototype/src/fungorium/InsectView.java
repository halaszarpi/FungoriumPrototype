package fungorium;

public class InsectView {

    Insect insect;

    public InsectView(Insect insect) {
        this.insect = insect;
    }

    public void insectInitialized() {
        System.out.println("Insect (" + insect.getName() + ") is initialized on (" + insect.getTecton().getName() + ") tecton!");
    }

    public void insectIsAntiSevered() {
        System.out.println("Insect (" + insect.getName() + ") is anti-severed for " + insect.getAntiSeveredForRounds() + " rounds!");
    }

    public void insectIsStunned() {
        System.out.println("Insect (" + insect.getName() + ") is stunned for " + insect.getStunnedForRounds() + " rounds!");
    }

    public void insectIsSlowed() {
        System.out.println("Insect (" + insect.getName() + ") is slowed for " + insect.getSlowedForRounds() + " rounds!");
    }

    public void insectIsBoosted() {
        System.out.println("Insect (" + insect.getName() + ") is an boosted for " + insect.getBoostedForRounds() + " rounds!");
    }

    public void insectAteSpore(Spore spore) {
        System.out.println("Insect (" + insect.getName() + ") ate (" + spore.getName() + ") spore!");
    }

    public void insectSteppedToTecton() {
        System.out.println("Insect (" + insect.getName() + ") stepped to (" + insect.getTecton().getName() + ") tecton!");
    }

    public void insectCutMycelium(Tecton targetTecton) {
        System.out.println("Insect (" + insect.getName() + ") cut mycelium between tectons (" + insect.getTecton().getName() + ") and " + targetTecton.getName() + "!");

    }

    public void insectEffectsReduced() {
        System.out.println("Active effects on insect (" + insect.getName() + ") were reduced by 1!");
    }

    public void insectGotEaten() {
        System.out.println("Insect (" + insect.getName() + ") got eaten!");
    }

    // Ide esetleg nem kellene az uj insect neve?
    public void insectDuplicated() {
        System.out.println("Insect (" + insect.getName() + ") is duplicated!");
    }

}