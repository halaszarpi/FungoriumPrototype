package fungorium;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
        mycelium.getBody().getGFungusBody().setCoordinates(mycelium.getGMycelium().getCoords().x, mycelium.getGMycelium().getCoords().y);
        mycelium.getGMycelium().attach(mycelium.getBody().getView());
    }

    // remelem jo
    public void bodyHasDied() {
        mycelium.getGMycelium().detach(mycelium.getBody().getView());
    }

    public String hasNoFungusBody() {
        String message = "\nMycelium (" + mycelium.getName() + ") cannot scatter spore: no FungusBody present!\n";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    public void myceliumHasDied() {
        mycelium.getTecton().getGTecton().detach(mycelium.getView());
    }

    public void ateInsect(Insect insect) {
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

    public String invalidTarget(Tecton targetTecton) {
        String message = "Cannot spread to tecton (" + targetTecton.getName() + ")!";
        JOptionPane.showMessageDialog(null, message);
        return message;
    }

    /**
     * The graphical representation of the mycelium, it consists of a line with the color of the FungusFarmer
     */
    @Override
    public void draw(Graphics g, Point coords) {
        FungusFarmerView fmv = (FungusFarmerView)mycelium.getOwner().getView();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(fmv.getColor());
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(coords.x - xOffset, coords.y, coords.x + xOffset, coords.y);
    }

    public Mycelium getMycelium () { return mycelium; }
}