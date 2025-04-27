package fungorium;

public class FungusFarmerView {

    FungusFarmer fungusFarmer;

    public FungusFarmerView(FungusFarmer fungusFarmer) {
        this.fungusFarmer = fungusFarmer;
    }

    public void chooseAction() {
        System.out.println(fungusFarmer.toString());
        System.out.println("Enter command:\n\tGROWMYC(2)\n\tGROWBOD(1)\n\tSCATTERSP(1)\n\tEATINS(3)\n\tSKIP");
    }

    public void myceliumInitialized(Tecton startingTecton) {
        System.out.println("Starting mycelium for (" + fungusFarmer.getName() + ") has been initialized on tecton (" + startingTecton.getName() +") !");
    }

    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }
}
