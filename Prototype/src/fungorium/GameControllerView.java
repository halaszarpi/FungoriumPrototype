package fungorium;

public class GameControllerView {

  
    public void ErrorCreatingMap(Exception e){
        System.out.println("Error while creating map: "+ e.getMessage());
    }

    public void Startedprint(){
        System.out.println("New game started!");
        System.out.println("Please enter the number of players:");
    }

    public void PlayerisFungusFarmer(int i){
        System.out.println("Player " + (i + 1) + " is a Fungus Farmer. Please choose a name:");
    }

    public void PlayerisInsectKeeper(int i){
        System.out.println("Player " + (i + 1) + " is an Insect Keeper. Please choose a name:");
    }
    
    public void NumberofRounds(){
        System.out.println("Please enter the number of rounds:");
    }

    public void MapisReady(){
        System.out.println("The game map is ready!");
    }

    public void ChoosingstartTecton(String playername){
        System.out.println("Player " + playername + " is choosing a starting Tecton:");
    }

    public void ErrorChoosingstartTecton(Exception e){
        System.out.println("Error while choosing starting Tecton: " + e.getMessage());
    }

    public void ErrorInitializePlayer(Exception e){
        System.out.println("Error while initializing player: " + e.getMessage());
    }

    public void Initializesuccessfull(){
        System.out.println("Game initialized!");
    }

    public void EndGame(){
        System.out.println("Game ended!");
    }

}
