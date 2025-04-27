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

        while(actionPoints > 0 && inGame) {

            view.chooseAction();

            String command = in.nextLine();
            String[] args = command.split(" ");

            if((args.length != 3) && !args[0].equals("SKIP") ) {
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
        String myceliumName = args[1];
        Mycelium mycelium = map.findMycelium(myceliumName);
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
            case "SKIP":
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
    public void initializePlayer(Tecton startingTecton) {
        Mycelium mycelium = new Mycelium(getNewMyceliumName(), this, startingTecton);
        myceliums.add(mycelium);
        try {
            startingTecton.addMycelium(mycelium);
            view.myceliumInitialized(startingTecton);
        }
        catch (Exception e) {
            System.out.println("Error while adding mycelium to tecton: " + e.getMessage());
        }
    }

    public void addMycelium(Mycelium mycelium){
        myceliums.add(mycelium);
    }

    public void addSpore(Spore spore){
        spores.add(spore);
    }

    public String getNewMyceliumName() {
        return (this.name + "-m" + (myceliums.size() + 1));
    }

    public List<Mycelium> getMyceliums() {
        return myceliums;
    }
}