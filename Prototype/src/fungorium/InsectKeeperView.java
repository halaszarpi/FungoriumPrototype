package fungorium;

public class InsectKeeperView {
    InsectKeeper insectKeeper;

    public InsectKeeperView(InsectKeeper insectKeeper) {this.insectKeeper = insectKeeper;}

    public void chooseAction() {
        System.out.println(insectKeeper.toString());
        System.out.println("Enter command:\n\tMOVETOTECTON(1-3)\n\t CUTMYC(1)\n\t EATSPORE(1)\n\t SKIP");
    }

    public void insectInitialized(Tecton startingTecton) {
        System.out.println("Starting insect for (" + insectKeeper.getName() + ") has been initialized on tecton (" + startingTecton.getName() +") !");
    }

    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }
}