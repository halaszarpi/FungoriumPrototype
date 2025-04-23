package fungorium;

public class InsectView {

    // Biztos fog valami belole kimaradni

    public void insectInitialized(String insectName, String tectonName) {
        System.out.println("Insect (" + insectName + ") is initialized on (" + tectonName + ") tecton!");
    }

    public void insectIsAntiSevered(String insectName, int numberOfRounds) {
        System.out.println("Insect (" + insectName + ") is anti-severed for " + numberOfRounds + " rounds!");
    }

    public void insectIsStunned(String insectName, int numberOfRounds) {
        System.out.println("Insect (" + insectName + ") is stunned for " + numberOfRounds + " rounds!");
    }

    public void insectIsSlowed(String insectName, int numberOfRounds) {
        System.out.println("Insect (" + insectName + ") is slowed for " + numberOfRounds + " rounds!");
    }

    public void insectIsBoosted(String insectName, int numberOfRounds) {
        System.out.println("Insect (" + insectName + ") is an boosted for " + numberOfRounds + " rounds!");
    }

    public void insectAteSpore(String insectName, String sporeName) {
        System.out.println("Insect (" + insectName + ") ate (" + sporeName + ") spore!");
    }

    public void insectSteppedToTecton(String insectName, String targetTectonName) {
        System.out.println("Insect (" + insectName + ") stepped to (" + targetTectonName + ") tecton!");
    }

    public void insectCutMycelium(String insectName, String sourceTectonName, String targetTectonName) {
        System.out.println("Insect (" + insectName + ") cut mycelium between tectons (" + sourceTectonName + ") and " + targetTectonName + "!");

    }

    public void insectEffectsReduced(String insectName) {
        System.out.println("Active effects on insect (" + insectName + ") were reduced by 1!");
    }

    public void insectGotEaten(String insectName) {
        System.out.println("Insect (" + insectName + ") got eaten!");
    }

    // Ide esetleg nem kellene az uj insect neve?
    public void insectDuplicated(String insectName) {
        System.out.println("Insect (" + insectName + ") is duplicated!");
    }

}
