package fungorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The InsectKeeper class represents a player who controls a collection of
 * insects.
 * This class extends the {@link Player} class and is responsible for managing
 * the insect-related actions and commands in the game, as well as performing
 * actions that affect the game state, such as moving insects or interacting
 * with spores.
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
     * Changes the game map state based on the given commands.
     * Executes actions like moving an insect, cutting mycelium, or eating a spore.
     *
     * @param map  The current TectonMap of the game.
     * @param args The command arguments specifying the action and its parameters.
     * @throws Exception If an invalid action is attempted or an error occurs during
     *                   the action.
     */
    public void changeMapBasedOnCommands(TectonMap map, String[] args) throws Exception {

        String action = args[0].toUpperCase();
        String insectName = args.length > 1 ? args[1] : null;
        Insect insect = map.findInsect(insectName);
        String targetName = args.length > 2 ? args[2] : null;

        // Ezt meg itt hagyom ha gond lenne
        /*
        int cost = switch (action) {
        case "MOVETOTECTON" -> 2;
        case "MOVETOTECTON":
        case "CUTMYC", "EATSPORE" -> 1;
        default -> 0;
        */

        int cost;
        switch (action) {
            case "MOVETOTECTON" -> {
                if (insect.getBoostedForRounds() > 0) {
                    cost = 1;
                } else if (insect.getSlowedForRounds() > 0) {
                    cost = 3;
                } else {
                    cost = 2;
                }
            }
            case "CUTMYC", "EATSPORE" -> cost = 1;
            default -> cost = 0; // elvileg nem kellene bajnak lennie
        }

        if (cost > actionPoints) {
            view.notEnoughActionPoints();
            return;
        }

        // Innen a findInsect-eket kivettem es beraktam az elejere
        switch (action) {
            case "MOVETOTECTON":
                try {
                    Tecton targetTecton1 = map.findTecton(targetName);
                    insect.stepToTecton(targetTecton1);
                } catch (Exception exp) {
                    exp.getMessage();
                }
                break;
            case "CUTMYC":
                try {
                    Mycelium targetMycelium = map.findMycelium(targetName);
                    insect.cutMycelium(targetMycelium);
                } catch (Exception exp) {
                    exp.getMessage();
                }
                break;
            case "EATSPORE":
                try {
                    Spore targetSpore = map.findSpore(targetName);
                    insect.eatSpore(targetSpore);
                } catch (Exception exp) {
                    exp.getMessage();
                }
                break;
            default:
                view.invalidActionMessage();
        }
    }

    /**
     * Removes an insect from the InsectKeeper's list and the tecton it's on when it
     * dies.
     *
     * @param insect The insect that has died.
     */
    public void insectDied(Insect insect) {
        insects.remove(insect);
        insect.getTecton().removeInsect(insect);
    }

    /**
     * Duplicates an insect, creating a new instance of it and adding it to the
     * InsectKeeper's list.
     *
     * @param insect The insect to duplicate.
     */
    public void duplicateInsect(Insect insect) {
        Insect duplicatedInsect = new Insect(getNewInsectName(), insect.getTecton(), insect.getOwner());
        insects.add(duplicatedInsect);
    }

    /**
     * Advances all insects controlled by the InsectKeeper by one round, reducing
     * the duration of their effects.
     */
    @Override
    public void roundPassed() {
        for (Insect insect : insects) {
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

    @Override
    public IObserver getView() {
        return (IObserver) view;
    }

    @Override
    public List<String> getActions() {
        List<String> actions = new ArrayList<>();
        actions.add("MOVETOTECTON (1-3)");
        actions.add("CUTMYC (1)");
        actions.add("EATSPORE (1)");
        return actions;
    }

    @Override
    public List<String> getParam1ForAction(String action) {
        List<String> parameters = new ArrayList<>();
        for (Insect insect : insects) {
            parameters.add(insect.getName() + " (" + insect.getTecton().getName() + ")");
        }
        return parameters;
    }

    @Override
    public List<String> getParam2ForAction(String action, String param1, GMap map) {
        List<String> parameters = new ArrayList<>();
        Insect i = map.findInsectByName(param1);
        switch (action) {
            case "MOVETOTECTON":
                for (Tecton tecton : map.getTectons()) {
                    if (tecton.isConnectedTo(i.getTecton())) {
                        parameters.add(tecton.getName());
                    }
                }
                break;
            case "CUTMYC":
                Tecton t = i.getTecton();
                List<Mycelium> myceliumList = t.getConnectedMyceliums();
                for (Mycelium m : myceliumList) {
                    parameters.add(m.getName());
                }
                break;
            case "EATSPORE":
                List<Spore> spores = i.getTecton().getSporeList();
                for (Spore spore : spores) {
                    parameters.add(spore.getName());
                }
                break;
        }
        return parameters;
    }

    @Override
    public void doAction(TectonMap map, String commandToRun) {
        String[] args = commandToRun.split(" ");
        try {
            changeMapBasedOnCommands(map, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}