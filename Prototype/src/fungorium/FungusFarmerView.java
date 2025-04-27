package fungorium;

public class FungusFarmerView {

    public void chooseAction() {
        System.out.println("Enter command:\n\tGROWMYC(2)\n\tGROWBOD(1)\n\tSCATTERSP(1)\n\tEATINS(3)\n\tSKIP");
    }

    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }
}
