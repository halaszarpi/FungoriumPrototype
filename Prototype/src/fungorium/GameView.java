package fungorium;

public class GameView {
    public void showMap(List<Tecton> map) {
        System.out.println("Current map state:");
        for (Tecton tecton : map) {
            System.out.println(tecton.ToString());
        }
    }

    public void refreshMap(List<Tecton> map) {
    }
}
