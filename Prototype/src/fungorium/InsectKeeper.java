package fungorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InsectKeeper extends Player {
    private List<Insect> insects;
    private InsectKeeperView view;

    public InsectKeeper(String name) {
        super(name);
        insects = new ArrayList<>();
        view = new InsectKeeperView(this);
    }

    @Override
    public void turn(List<Tecton> map, Scanner in) {

        while (actionPoints > 0 && inGame) {

            view.chooseAction();

            String command = in.nextLine();
            String[] args = command.split(" ");

            if ((args.length < 3 || args.length > 4) && !args[0].equals("SKIP")) {
                view.invalidActionMessage();
                continue;

            }
            String action = args[0];
            String insectName = args[1];
            Insect insect = findInsectByName(insectName, map);
            String targetName = args.length > 2 ? args[2] : null;

            try {
                switch (action) {
                    case "MOVETOTECTON":
                        Tecton targetTecton1 = findTectonByName(targetName, map);
                        insect.stepToTecton(targetTecton1);
                        break;
                    case "CUTMYC":
                        Tecton targetTecton2 = findTectonByName(targetName, map);
                        insect.cutMycelium(targetTecton2);
                        break;
                    case "EATSPORE":
                        Spore targetSpore = findSporeByName(targetName, map);
                        insect.eatSpore(targetSpore);
                        break;
                    case "SKIP":
                        break;
                    default:
                        view.invalidActionMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            if (insects.isEmpty()) {
                inGame = false;
            }

        }

    }

    public void insectDied(Insect insect) {
        insects.remove(insect);
        insect.getTecton().removeInsect(insect);
    }

    public void duplicateInsect(Insect insect) {
        String duplicatedInsectName = this.name + "-i" + insects.size() + 1;
        Insect duplicatedInsect = new Insect(duplicatedInsectName, insect.getTecton(), insect.getOwner());
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
        Insect insect = new Insect("I1", startingTecton, this);
        insects.add(insect);
        try {
            startingTecton.addInsect(insect);
            view.insectInitialized(startingTecton);
        } catch (Exception e) {
            System.out.println("Error while adding insect to tecton: " + e.getMessage());
        }
    }
}