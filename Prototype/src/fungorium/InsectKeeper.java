package fungorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The InsectKeeper class represents a player who controls a collection of insects.
 * This class extends the {@link Player} class and is responsible for managing
 * the insect-related actions and commands in the game, as well as performing
 * actions that affect the game state, such as moving insects or interacting with spores.
 */
public class InsectKeeper extends Player {
    private List<Insect> insects;
    private final InsectKeeperView view;

    /**
     * Constructor for creating a new InsectKeeper with the given name.
     *
     * @param name The name of the InsectKeeper.
     */
    public InsectKeeper(String name) {
        super(name);
        insects = new ArrayList<>();
        view = new InsectKeeperView(this);
    }

    /**
     * The main method for handling a single turn of the InsectKeeper.
     * The player can perform actions such as moving insects, cutting mycelium,
     * or eating spores, as well as checking the map or skipping their turn.
     *
     * @param map The current TectonMap of the game.
     * @param in The scanner for reading user input.
     */
    @Override
    public void turn(TectonMap map, Scanner in) {
        if (insects.isEmpty()) {
            inGame = false;
            return;
        }
        actionPoints = 4;

        while (actionPoints > 0 && inGame) {
            view.chooseAction();

            String command = in.nextLine();
            String[] args = command.split(" ");

            if ((args.length != 3) && !args[0].equalsIgnoreCase("SKIP") && !args[0].equalsIgnoreCase("INFO") && !args[0].equalsIgnoreCase("SHOWMAP")) {
                System.out.println("Invalid command");
                continue;
            }

            try {
                changeMapBasedOnCommands(map, args);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }

    /**
     * Changes the game map state based on the given commands.
     * Executes actions like moving an insect, cutting mycelium, or eating a spore.
     *
     * @param map The current TectonMap of the game.
     * @param args The command arguments specifying the action and its parameters.
     * @throws Exception If an invalid action is attempted or an error occurs during the action.
     */
    public void changeMapBasedOnCommands(TectonMap map, String[] args) throws Exception {

        String action = args[0].toUpperCase();
        String insectName = args.length > 1 ? args[1] : null;
        Insect insect;
        String targetName = args.length > 2 ? args[2] : null;

        switch (action) {
            case "MOVETOTECTON":
                Tecton targetTecton1 = map.findTecton(targetName);
                insect = map.findInsect(insectName);
                insect.stepToTecton(targetTecton1);
                break;
            case "CUTMYC":
                Tecton targetTecton2 = map.findTecton(targetName);
                insect = map.findInsect(insectName);
                insect.cutMycelium(targetTecton2);
                break;
            case "EATSPORE":
                Spore targetSpore = map.findSpore(targetName);
                insect = map.findInsect(insectName);
                insect.eatSpore(targetSpore);
                break;
            case "INFO":
                view.info();
                break;
            case "SHOWMAP":
                map.showMap();
                break;
            case "SKIP":
                actionPoints=0;
                break;
            default:
                view.invalidActionMessage();
        }
    }

    /**
     * Removes an insect from the InsectKeeper's list and the tecton it's on when it dies.
     *
     * @param insect The insect that has died.
     */
    public void insectDied(Insect insect) {
        insects.remove(insect);
        insect.getTecton().removeInsect(insect);
    }

    /**
     * Duplicates an insect, creating a new instance of it and adding it to the InsectKeeper's list.
     *
     * @param insect The insect to duplicate.
     */
    public void duplicateInsect(Insect insect) {
        Insect duplicatedInsect = new Insect(getNewInsectName(), insect.getTecton(), insect.getOwner());
        insects.add(duplicatedInsect);
    }

    /**
     * Advances all insects controlled by the InsectKeeper by one round, reducing the duration of their effects.
     */
    @Override
    public void roundPassed(){
        for(Insect insect: insects){
            insect.roundPassed();
        }
    }

    /**
     * Initializes a new insect for the InsectKeeper at a given starting tecton.
     *
     * @param startingTecton The tecton where the new insect is placed.
     */
    @Override
    public void initializePlayer(Tecton startingTecton) {
        Insect insect = new Insect(getNewInsectName(), startingTecton, this);
        insects.add(insect);
        try {
            startingTecton.addInsect(insect);
        } catch (Exception e) {
            System.out.println("Error while adding insect to tecton: " + e.getMessage());
        }
    }

    /**
     * Generates a new unique name for an insect controlled by this InsectKeeper.
     *
     * @return A unique insect name.
     */
    public String getNewInsectName() {
        return (this.name + "-i" + (insects.size() + 1));
    }

    /**
     * Returns the list of insects controlled by this InsectKeeper.
     *
     * @return A list of insects.
     */

    public List<Insect> getInsects() {
        return insects;
    }

    public void addInsect(Insect insect) {
        insects.add(insect);
    }

    public void removeInsect(Insect insect) {
        insects.remove(insect);
    }
    
}