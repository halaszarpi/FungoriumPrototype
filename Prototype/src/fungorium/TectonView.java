package fungorium;

public class TectonView {

    Tecton t;

    public TectonView(Tecton tecton) {
        this.t = tecton;
    }

    public String notNeighbour(Tecton t2) { return "The tectons ("+ t.getName() + ", " + t2.getName() + ") are not neighbours!"; }

    public String notNeighbourOrNeigboursNeighbour(Tecton t2) { return "The tectons ("+ t.getName() + ", " + t2.getName() + ") are not neighbours and not neighbour to any neighbour!"; }

    public String notConnectedByMycelium(Tecton t2) { return "The tectons ("+ t.getName() + ", " + t2.getName() + ") are not connected by mycelium!"; }

    public String tectonHasNoSpores() { return "The tecton (" + t.getName() + ") does not have spores!";}

    public String alreadyHasFungusbody() { return "There is already a fungus body on the tecton (" + t.getName() + ")!"; }

    public String singleMyceliumTectonAlreadyHasMycelium() { return "The single mycelium tecton (" + t.getName() + ") has already a mycelium on it!"; }

    public String noBodyTectonCannotPlaceBody() { return "Cannot place fungus body on no body tecton (" + t.getName() + ")!"; }

    public String  notMyceliumVanisherTecton() { return "Not Mycelium Vanisher Tecton (" + t.getName() + "), therefor cant vanish mycelium"; }

    public void tectonCreated() {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Tecton (" + t.getName() + ") is created!"); 
    }

    public void tectonBreaks(Tecton newTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Tecton (" + t.getName() + ") broke, new half tectons name: "+ newTecton.getName() + "!"); 
    }

    public void myceliumVanishes(Mycelium m) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Mycelium (" + m.getName() + ") vanished from mycelium vanisher tecton (" + t.getName() + ")!"); 
    }

    public void neighbourAdded(Tecton newTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Tecton (" + newTecton.getName() + ") is now a neighbour of (" + t.getName() + ")!"); 
    }

    public void connectionAdded(Tecton newTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Tecton (" + newTecton.getName() + ") is connected to (" + t.getName() + ")!"); 
    }

    public void myceliumAdded(Mycelium m) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Mycelium (" + m.getName() + ") is added on tecton (" + t.getName() + ")!"); 
    }

    public void sporeAdded(Spore s) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Spore (" + s.getName() + ") is added on tecton (" + t.getName() + ")!"); 
    }

    public void insectAdded(Insect i) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Insect (" + i.getName() + ") is added on tecton (" + t.getName() + ")!"); 
    }

    public void neighbourRemoved(Tecton newTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Tecton (" + newTecton.getName() + ") is no longer neighbour of (" + t.getName() + ")!"); 
    }

    public void myceliumRemoved(Mycelium m) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Mycelium (" + m.getName() + ") is no longer on tecton (" + t.getName() + ")!"); 
    }

    public void insectRemoved(Insect i) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Insect (" + i.getName() + ") is no longer on tecton (" + t.getName() + ")!"); 
    }

    public void sporeRemoved(Spore s) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Spore (" + s.getName() + ") is no longer on tecton (" + t.getName() + ")!"); 
    }

    public void removeConnection(Tecton newTecton) {
        if (!GameTesterController.SHOW_OUTPUT) { return; } 
        System.out.println("Tecton (" + newTecton.getName() + ") is no longer connected to (" + t.getName() + ")!"); 
    }

}