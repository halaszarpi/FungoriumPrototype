package fungorium;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JOptionPane;

public class MyceliumView implements IObserver{

    Mycelium mycelium;
    private final int xOffset = 3;

    public MyceliumView(Mycelium mycelium) {
        this.mycelium = mycelium;
    }

    public String notEnoughActionPointsForBody() {
        String message = "Not enough action points to grow a body!";
        JOptionPane.showMessageDialog(null, message);
        return message; 
    }

    public String alreadyHasABodyMessage() {
        String message = "This mycelium (" + mycelium.getName() + ") already has a body!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public String cannotPlaceBodyMessage() {
        String message = "Cannot place body on this tecton!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public String hasNoSporeToGrowBody() {
        String message = "No spores on the tecton to grow a body!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public void hasGrownBody() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        mycelium.getBody().getGFungusBody().setCoordinates(mycelium.getGMycelium().getCoords().x, mycelium.getGMycelium().getCoords().y);
        mycelium.getGMycelium().attach(mycelium.getBody().getView());
    }

    public void hasSpreadTo(Tecton targetTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium (" + mycelium.getName() + ") has spread to tecton (" + targetTecton.getName() + ")!");
    }

    public String invalidTarget(Tecton targetTecton) {
        return ("Tecton (" + targetTecton.getName() + "does not exist or not a neighbour to tecton (" + mycelium.getTecton().getName() + ")!");
    }

    public void bodyHasDied() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium's (" + mycelium.getName() + ") FungusBody has died!");
    }

    public String hasNoFungusBody() {
        String message = "\nMycelium (" + mycelium.getName() + ") cannot scatter spore: no FungusBody present!\n";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public void cooldownReduced() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Scattering cooldown reduced on mycelium's (" + mycelium.getName() + ") FungusBody by 1!");
    }

    public void myceliumHasDied() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        mycelium.getTecton().getGTecton().detach(mycelium.getView());
    }

    public void myceliumSustained() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Mycelium's (" + mycelium.getName() + ") life sustained!");
    }

    public void ateInsect(Insect insect) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        insect.getTecton().getGTecton().detach(insect.getView());
    }

    public String cannotEatInsect(Insect insect) {
        String message = "Insect (" + insect.getName() + ") can't be eaten (not stunned)!";
        JOptionPane.showMessageDialog(null, message);
        return message; 
    }

    public String targetAlreadyHasMycelium(Tecton targetTecton) {
        String message = "Tecton (" + targetTecton.getName() + ") already has mycelium on it and there is a connection between them!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public void connectionAddedTo(Tecton newTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Tecton (" + mycelium.getTecton().getName() + ") is reconnected to (" + newTecton.getName() + ")!");
    }

     // Újítás, kell Graphics g paraméterben
    @Override
    public void draw(Graphics g, Point coords) {
        FungusFarmerView fmv = (FungusFarmerView)mycelium.getOwner().getView();
        g.setColor(fmv.getColor());
        g.drawLine(coords.x - xOffset, coords.y, coords.x + xOffset, coords.y);
    }

    public Mycelium getMycelium () { return mycelium; }
}