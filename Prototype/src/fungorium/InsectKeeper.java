package fungorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InsectKeeper extends Player {
    private List<Insect> insects;
    private final InsectKeeperView view;

    public InsectKeeper(String name) {
        super(name);
        insects = new ArrayList<>();
        view = new InsectKeeperView(this);
    }

    @Override
    public void turn(TectonMap map, Scanner in) {

        while (actionPoints > 0 && inGame) {
            System.out.println("Enter name: " + name + "actionPoints" + actionPoints);
            System.out.println("Enter command:MOVETOTECTON(1-3)\n CUTMYC(1)\n EATSPORE(1)\n SKIP");

            String command = in.nextLine();
            String[] args = command.split(" ");

            if ((args.length < 3 || args.length > 4) && !args[0].equals("SKIP")) {
                System.out.println("Invalid command");
                continue;
            }

            try {
                changeMapBasedOnCommands(map, args);
            } catch (Exception e) {
                e.printStackTrace();

            }
            if (insects.isEmpty()) {
                inGame = false;
            }

        }

    }

    public void changeMapBasedOnCommands(TectonMap map, String[] args) throws Exception {

        String action = args[0].toUpperCase();
        String insectName = args[1];
        Insect insect = map.findInsect(insectName);
        String targetName = args.length > 2 ? args[2] : null;

        switch (action) {
            case "MOVETOTECTON":
                Tecton targetTecton1 = map.findTecton(targetName);
                insect.stepToTecton(targetTecton1);
                break;
            case "CUTMYC":
                Tecton targetTecton2 = map.findTecton(targetName);
                insect.cutMycelium(targetTecton2);
                break;
            case "EATSPORE":
                Spore targetSpore = map.findSpore(targetName);
                insect.eatSpore(targetSpore);
                break;
            case "SKIP":
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    public void insectDied(Insect insect) {
        insects.remove(insect);
        insect.getTecton().removeInsect(insect);
    }

    public void duplicateInsect(Insect insect) {
        Insect duplicatedInsect = new Insect(getNewInsectName(), insect.getTecton(), insect.getOwner());
        insects.add(duplicatedInsect);
    }

    @Override
    public void roundPassed(){
        for(Insect insect: insects){
            insect.roundPassed();
        }
    }

    @Override
    public void initializePlayer(Tecton startingTecton) {
        Insect insect = new Insect(getNewInsectName(), startingTecton, this);
        insects.add(insect);
        try {
            startingTecton.addInsect(insect);
            view.insectInitialized(startingTecton);
        } catch (Exception e) {
            System.out.println("Error while adding insect to tecton: " + e.getMessage());
        }
    }

    public String getNewInsectName() {
        return (this.name + "-i" + (insects.size() + 1));
    }
}