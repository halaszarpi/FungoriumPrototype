package fungorium;

import java.util.Scanner;

public class TectonMapView {

    public TectonMapView(TectonMap tectonMap) {
    }

    public String noSuchTecton(String tectonType) { return "No such tecton type(" + tectonType + ")!"; }

    public String couldntFindTectont(String name) { return "There is no tecton on the map with the name " + name + "!"; }

    public String couldntFindMycelium(String name) { return "There is no mycelium on the map with the name " + name + "!"; }

    public String couldntFindInsect(String name) { return "There is no insect on the map with the name " + name + "!"; }

    public String couldntFindSpore(String name) { return "There is no spore on the map with the name " + name + "!"; }
}