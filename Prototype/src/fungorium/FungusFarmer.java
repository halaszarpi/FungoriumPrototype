package fungorium;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a Fungus Farmer player in the Fungorium game.
 *
 * The Fungus Farmer controls Myceliums and Spores, grows fungus bodies,
 * and interacts with the map during their turn by scattering spores,
 * growing myceliums, eating insects, and more.
 */
public class FungusFarmer extends Player {
    private List<Mycelium> myceliums;
    private List<Spore> spores;
    private final FungusFarmerView view;

    /**
     * Constructs a new FungusFarmer with the given name.
     *
     * @param name the name of the player
     */
    public FungusFarmer(String name){
        super(name);
        myceliums = new ArrayList<>();
        spores = new ArrayList<>();
        view = new FungusFarmerView(this);
    }

    /**
     * Executes the Fungus Farmer's turn.
     *
     * @param map the TectonMap of the game
     * @param in the Scanner used for user input
     */
    @Override
    public void turn(TectonMap map, Scanner in){
        if (myceliums.isEmpty()) {
            inGame = false;
            return;
        }

        actionPoints = 4;

        while (actionPoints > 0 && inGame) {
            view.chooseAction();
            String command = in.nextLine();
            String[] args = command.split(" ");

            if ((args.length != 3) &&
                    !args[0].equalsIgnoreCase("SKIP") &&
                    !args[0].equalsIgnoreCase("INFO") &&
                    !args[0].equalsIgnoreCase("SHOWMAP")) {
                view.invalidActionMessage();
                continue;
            }

            try {
                changeMapBasedOnCommands(map, args, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Interprets and processes the given commands on the map.
     *
     * @param map the game map
     * @param args the command arguments
     * @throws Exception if an invalid action occurs
     */
    public void changeMapBasedOnCommands(TectonMap map, String[] args, boolean isTest) throws Exception {
        String action = args[0].toUpperCase();
        Mycelium mycelium = args.length > 1 ? map.findMycelium(args[1]) : null;
        String targetName = args.length > 2 ? args[2] : null;
        String sporeType = (args.length > 3  && isTest) ? args[3] : null;
        String sporeName = (args.length > 4 && isTest) ? args[4] : null;

        switch(action) {
            case "GROWMYC":
                Tecton targetTecton1 = map.findTecton(targetName);
                mycelium.spreadTo(targetTecton1);
                break;
            case "GROWBOD":
                Spore targetSpore1 = map.findSpore(targetName);
                mycelium.growBody(targetSpore1);
                break;
            case "SCATTERSP":
                Tecton targetTecton2 = map.findTecton(targetName);
                mycelium.scatterSpore(targetTecton2, sporeType, sporeName);
                break;
            case "EATINS":
                Insect targetInsect = map.findInsect(targetName);
                mycelium.eatInsect(targetInsect);
                break;
            case "INFO":
                view.info();
                break;
            case "SHOWMAP":
                map.showMap();
                break;
            case "SKIP":
                actionPoints = 0;
                break;
            default:
                view.invalidActionMessage();
                break;
        }
    }

    /**
     * Returns the list of spores owned by the player.
     *
     * @return the list of spores
     */
    public List<Spore> getSpores() {
        return this.spores;
    }

    /**
     * Notifies all myceliums that a new round has passed.
     */
    @Override
    public void roundPassed(){
        for (Mycelium mycelium : myceliums) {
            mycelium.roundPassed();
        }
    }

    /**
     * Initializes the player at the start of the game with a mycelium on the starting tecton.
     *
     * @param startingTecton the tecton where the player starts
     * @throws Exception if the initialization fails
     */
    @Override
    public void initializePlayer(Tecton startingTecton) throws Exception {
        Mycelium mycelium = new Mycelium(getNewMyceliumName(), this, startingTecton);
        myceliums.add(mycelium);
        startingTecton.addMycelium(mycelium);

        OrdinarySpore s = new OrdinarySpore(this, 0, 0, "initSpore");
        startingTecton.addSpore(s);

        try {
            mycelium.growBody(s);
        } catch (Exception e) {
            startingTecton.removeMycelium(mycelium);
            startingTecton.removeSpore(s);
            myceliums.remove(mycelium);
            throw e;
        }
        view.myceliumInitialized(startingTecton);
    }

    /**
     * Adds a mycelium to the player's list.
     *
     * @param mycelium the mycelium to add
     */
    public void addMycelium(Mycelium mycelium){
        myceliums.add(mycelium);
    }

    /**
     * Adds a spore to the player's list.
     *
     * @param spore the spore to add
     */
    public void addSpore(Spore spore){
        spores.add(spore);
    }

    /**
     * Removes a spore from the player's list.
     *
     * @param spore the spore to remove
     */
    public void removeSpore(Spore spore){
        spores.remove(spore);
    }

    public void removeMycelium(Mycelium mycelium) {
        myceliums.remove(mycelium);
    }

    /**
     * Generates a new name for a mycelium belonging to the player.
     *
     * @return the generated mycelium name
     */
    public String getNewMyceliumName() {
        return (this.name + "-m" + (myceliums.size() + 1));
    }

    /**
     * Generates a new name for a spore belonging to the player.
     *
     * @return the generated spore name
     */
    public String getNewSporeName() {
        return (this.name + "-s" + (spores.size() + 1));
    }

    /**
     * Returns the list of myceliums owned by the player.
     *
     * @return the list of myceliums
     */
    public List<Mycelium> getMyceliums() {
        return myceliums;
    }

    @Override
    public boolean equals(Object obj) {
        FungusFarmer otherFarmer = (FungusFarmer) obj;
        return this.name.equals(otherFarmer.getName());
    }
}