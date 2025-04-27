package fungorium;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class FungusFarmer extends Player {
    private List<Mycelium> myceliums;
    private List<Spore> spores;
    private final FungusFarmerView view;
    
    public FungusFarmer(String name){
        super(name);
        myceliums = new ArrayList<>();
        spores = new ArrayList<>();
        view = new FungusFarmerView(this);
    }

    @Override
    public void turn(List<Tecton> map, Scanner in){

        while(actionPoints > 0 && inGame){

            view.chooseAction();

            String command = in.nextLine();
            String[] args = command.split(" ");

            if((args.length != 3) && !args[0].equals("SKIP") ) {
                view.invalidActionMessage();
                continue;
            }

            String action = args[0];
            String myceliumName= args[1];
            Mycelium mycelium = findMyceliumByName(myceliumName, map);
            String targetName = args.length > 2 ? args[2] : null;

            try {
                switch(action) {
                    case "GROWMYC":
                        Tecton targetTecton1 = findTectonByName(targetName,map);
                        mycelium.spreadTo(targetTecton1);
                        break;
                    case"GROWBOD":
                        Spore targetSpore1 = findSporeByName(targetName, map);
                        mycelium.growBody(targetSpore1); // ez az a spora, amit felhasznal a noveszteshez
                        break;
                    case "SCATTERSP":
                        Tecton targetTecton2 = findTectonByName(targetName,map);
                        mycelium.scatterSpore(targetTecton2);
                        break;
                    case "EATINS":
                        Insect targetInsect = findInsectByName(targetName,map);
                        mycelium.eatInsect(targetInsect);
                        break;
                    case "SKIP":
                        break;
                    default:
                        view.invalidActionMessage();
                        break;
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        
            if (myceliums.isEmpty()){
                inGame = false;
            }
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
        Mycelium mycelium = new Mycelium("M1", this, startingTecton);
        myceliums.add(mycelium);
        try {
            startingTecton.addMycelium(mycelium);
            view.myceliumInitialized(startingTecton);
        }
        catch (Exception e) {
            System.out.println("Error while adding mycelium to tecton: " + e.getMessage());
        }
    }

}