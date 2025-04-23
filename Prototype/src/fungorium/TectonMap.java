package fungorium;

import java.util.List;
import java.util.Scanner;

public class TectonMap {
    List<Tecton> tectons;
    private final TectonMapView view;

    public TectonMap(List<Tecton> _tectons, Scanner _scanner) {
        this.tectons = _tectons;
        this.view = new TectonMapView(_scanner);
    }

    public TectonMap(String url, Scanner _scanner) {
        /// TODO
        //Load map from file "map.txt"
        //this.tectons = ...

        this.view = new TectonMapView(_scanner);
    }

    public List<Tecton> map(){
        return tectons;
    }

    public void showMap() {
        view.showMap(this);
    }

    public void refreshMap() {
        view.refreshMap(this);
    }

}
