package fungorium;

public class FungusFarmerView {

    FungusFarmer fungusFarmer;

    public FungusFarmerView(FungusFarmer fungusFarmer) {
        this.fungusFarmer = fungusFarmer;
    }

    public void chooseAction() {
        System.out.println(fungusFarmer.toString());
        System.out.println("Enter command:\n\tGROWMYC(2) [MYCNAME] [TECNAME]\n\tGROWBOD(2) [MYCNAME] [SPONAME]\n\tSCATTERSP(1) [MYCNAME] [TECNAME]\n\tEATINS(3) [MYCNAME] [INSNAME]\n\tINFO\n\tSHOWMAP\n\tSKIP");
    }

    public void myceliumInitialized(Tecton startingTecton) {
        System.out.println("Starting mycelium for (" + fungusFarmer.getName() + ") has been initialized on tecton ("
                + startingTecton.getName() + ") !");
    }

    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }

    public void info() {
        System.out.println("FUNGUS FARMER INFO");
        System.out.println("--------------------------");
        System.out.println("\tNAME: "+fungusFarmer.getName());
        System.out.println("\tACTIONPOINTS: "+fungusFarmer.getActionPoints());
        System.out.println("\tSCORE: "+fungusFarmer.getScore());
        System.out.println("\nMYCELIUMS: ");

        for(Mycelium mycelium : fungusFarmer.getMyceliums()){
            System.out.println("\t" + mycelium.getName() + " on tecton (" + mycelium.getTecton().getName() + ")");
        }

        System.out.println("\nFUNGUSBODYS:");
        for(Mycelium mycelium: fungusFarmer.getMyceliums()){
            if(mycelium.hasBody()){
                mycelium.getBody().toString();
            }
        }
        
        System.out.println("\nSPORES:");
        for(Spore spore:fungusFarmer.getSpores()){
            System.out.println("\t" + spore.getName());
        }

        System.out.println("--------------------------");
    }

}