package fungorium;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class FungusFarmer extends Player {
    private List<Mycelium> myceliums;
    private List<Spore> spores;
    private FungusFarmerView view;

    public FungusFarmer(String name){
        super(name);
        myceliums = new ArrayList<>();
        spores = new ArrayList<>();
        view = new FungusFarmerView(this);
    }

    @Override
    public void turn(TectonMap map, Scanner in){
        actionPoints = 4;

        while(actionPoints > 0 && inGame) {
            view.chooseAction();

            String command = in.nextLine();
            String[] args = command.split(" ");

            if((args.length != 3) && !args[0].equalsIgnoreCase("SKIP") && !args[0].equalsIgnoreCase("INFO") && !args[0].equalsIgnoreCase("SHOWMAP")) {
                view.invalidActionMessage();
                continue;
            }

            try {
                changeMapBasedOnCommands(map, args);

            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    public void changeMapBasedOnCommands(TectonMap map, String[] args) throws Exception {

        if (myceliums.isEmpty()) {
            inGame = false;
        }

        String action = args[0].toUpperCase();
        Mycelium mycelium = args.length > 1 ? map.findMycelium(args[1]): null;
        String targetName = args.length > 2 ? args[2] : null;

        switch(action) {
            case "GROWMYC":
                Tecton targetTecton1 = map.findTecton(targetName);
                mycelium.spreadTo(targetTecton1);
                break;
            case"GROWBOD":
                Spore targetSpore1 = map.findSpore(targetName);
                mycelium.growBody(targetSpore1); // ez az a spora, amit felhasznal a noveszteshez
                break;
            case "SCATTERSP":
                Tecton targetTecton2 = map.findTecton(targetName);
                mycelium.scatterSpore(targetTecton2);
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

    public List<Spore> getSpores() { return this.spores; }

    @Override
    public void roundPassed(){
        for(Mycelium mycelium : myceliums){
            mycelium.roundPassed();
        }
    }

    @Override
    public void initializePlayer(Tecton startingTecton) throws Exception {
        Mycelium mycelium = new Mycelium(getNewMyceliumName(), this, startingTecton);
        myceliums.add(mycelium);
        startingTecton.addMycelium(mycelium);

        OrdinarySpore s = new OrdinarySpore(this,0,0,"initSpore");
        startingTecton.addSpore(s);

        try {
            mycelium.growBody(s);
        }
        catch (Exception e){
            startingTecton.removeMycelium(mycelium);
            startingTecton.removeSpore(s);
            myceliums.remove(mycelium);
            throw e;
        }
        view.myceliumInitialized(startingTecton);
    }

    public void addMycelium(Mycelium mycelium){
        myceliums.add(mycelium);
    }

    public void addSpore(Spore spore){
        spores.add(spore);
    }

    public void removeSpore(Spore spore ){
        spores.remove(spore);
    }

    public String getNewMyceliumName() {
        return (this.name + "-m" + (myceliums.size() + 1));
    }

    public String getNewSporeName() {
        return (this.name + "-s" + (spores.size() + 1));
    }

    public List<Mycelium> getMyceliums() {
        return myceliums;
    }
}