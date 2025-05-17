package fungorium;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class InsectView implements IObserver {

    Insect insect;
    private final int xOffset = 5;
    private final int yOffset = 5;

    public InsectView(Insect insect) {
        this.insect = insect;
    }

    public void insectInitialized() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") is initialized on (" + insect.getTecton().getName() + ") tecton!");
    }

    public void insectIsAntiSevered() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") is anti-severed for " + insect.getAntiSeveredForRounds() + " rounds!");
    }

    public void insectIsStunned() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") is stunned for " + insect.getStunnedForRounds() + " rounds!");
    }

    public void insectIsSlowed() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") is slowed for " + insect.getSlowedForRounds() + " rounds!");
    }

    public void insectIsBoosted() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") is an boosted for " + insect.getBoostedForRounds() + " rounds!");
    }

    public void insectAteSpore(Spore spore) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") ate (" + spore.getName() + ") spore!");
    }

    public void insectSteppedToTecton() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") stepped to (" + insect.getTecton().getName() + ") tecton!");
    }

    public void insectCutMycelium(Tecton targetTecton) {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") cut mycelium between tectons (" + insect.getTecton().getName() + ") and " + targetTecton.getName() + "!");
    }

    public void insectEffectsReduced() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Active effects on insect (" + insect.getName() + ") were reduced by 1!");
    }

    public void insectGotEaten() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") got eaten!");
    }

    // Ide esetleg nem kellene az uj insect neve?
    public void insectDuplicated() {
        //if (!GameTesterController.SHOW_OUTPUT) { return; }
        System.out.println("Insect (" + insect.getName() + ") is duplicated!");
    }

    public String insectNotAbleToStep(String insectName, String targetTectonName) {
        return "Insect " + insectName + " is not able to step to tecton (" + targetTectonName + ")!";
    }

    public Insect getInsect() {
        return insect;
    }

    @Override
    public void draw(Graphics g, Point coords) {
        // a pont amit itt megkap a tekton kp.-ja
        // ez csak valami random egyelore
        g.setColor(Color.yellow);
        Polygon p = new Polygon(
            new int[] { (coords.x), (coords.x + xOffset), (coords.x - xOffset)},
            new int[] { (coords.y), (coords.y + yOffset), (coords.y + yOffset)},
            3);
        g.fillPolygon(p);
        g.drawPolygon(p);
    }

}