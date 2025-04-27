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
        actionPoints = 4;

        while (actionPoints > 0 && inGame) {
            view.chooseAction();

            String command = in.nextLine();
            String[] args = command.split(" ");

            if ((args.length != 3) && !args[0].equalsIgnoreCase("SKIP") && !args[0].equalsIgnoreCase("INFO")) {
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
            case "SKIP":
                actionPoints=0;
                break;
            default:
                view.invalidActionMessage();
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
        } catch (Exception e) {
            System.out.println("Error while adding insect to tecton: " + e.getMessage());
        }
    }

    public String getNewInsectName() {
        return (this.name + "-i" + (insects.size() + 1));
    }

    public List<Insect> getInsects() {
        return insects;
    }
}