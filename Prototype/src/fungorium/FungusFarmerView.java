package fungorium;

/**
 * This class represents the view for the FungusFarmer player in the game.
 *
 * It provides methods to display various information and options to the player,
 * such as available actions, mycelium initialization, invalid actions, and detailed player info.
 */
public class FungusFarmerView {

    private FungusFarmer fungusFarmer;

    /**
     * Constructs a FungusFarmerView for the given FungusFarmer.
     *
     * @param fungusFarmer the FungusFarmer this view belongs to
     */
    public FungusFarmerView(FungusFarmer fungusFarmer) {
        this.fungusFarmer = fungusFarmer;
    }

    /**
     * Displays the available actions to the player during their turn.
     * It shows a list of possible commands the player can input to perform various actions.
     */
    public void chooseAction() {
        System.out.println(fungusFarmer.toString());
        System.out.println("Enter command:\n\tGROWMYC(2) [MYCNAME] [TECNAME]\n\tGROWBOD(2) [MYCNAME] [SPONAME]\n\tSCATTERSP(1) [MYCNAME] [TECNAME]\n\tEATINS(3) [MYCNAME] [INSNAME]\n\tINFO\n\tSHOWMAP\n\tSKIP");
    }

    /**
     * Informs the player that their mycelium has been initialized on the specified tecton.
     *
     * @param startingTecton the tecton where the mycelium is initialized
     */
    public void myceliumInitialized(Tecton startingTecton) {
        System.out.println("Starting mycelium for (" + fungusFarmer.getName() + ") has been initialized on tecton ("
                + startingTecton.getName() + ") !");
    }

    /**
     * Displays an invalid action message to the player.
     * This is shown when the player enters a command that is not recognized or invalid.
     */
    public void invalidActionMessage() {
        System.out.println("Invalid action!");
    }

    /**
     * Displays detailed information about the FungusFarmer, including their action points,
     * score, mycelium(s), fungus body, and spores.
     */
    public void info() {
        System.out.println("FUNGUS FARMER INFO");
        System.out.println("--------------------------");
        System.out.println("\tNAME: " + fungusFarmer.getName());
        System.out.println("\tACTIONPOINTS: " + fungusFarmer.getActionPoints());
        System.out.println("\tSCORE: " + fungusFarmer.getScore());
        System.out.println("\nMYCELIUMS: ");

        for (Mycelium mycelium : fungusFarmer.getMyceliums()) {
            System.out.println("\t" + mycelium.getName() + " on tecton (" + mycelium.getTecton().getName() + ")");
        }

        System.out.println("\nFUNGUSBODYS:");
        for (Mycelium mycelium : fungusFarmer.getMyceliums()) {
            if (mycelium.hasBody()) {
                mycelium.getBody().toString();
            }
        }

        System.out.println("\nSPORES:");
        for (Spore spore : fungusFarmer.getSpores()) {
            System.out.println("\t" + spore.getName());
        }

        System.out.println("--------------------------");
    }
}