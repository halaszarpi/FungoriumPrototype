package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class GInsect implements IObservable {
    private Insect insect;
    private int x;
    private int y;
    private final int radius = 65;
    private ArrayList<IObserver> observers;

    public GInsect(Insect i) {
        this.insect = i;

        // Ezek egyelore csak kezdoertekek, majd valtozik
        this.x = 0;
        this.y = 0;
        observers = new ArrayList<>();
    }

    public void attach(IObserver o) {
        observers.add(o);
    }

    public void detach(IObserver o) {
        observers.remove(o);
    }

    public IObserver getObserver() { return this.observers.getFirst(); }

    public Point getCoords() { return new Point(x, y); }
    
    // itt az x es y parameterek ezek:
    // x = sin(insectPhase + pi/180)
    // y = cos(insectPhase + pi/180)
    public void setCoordinates(int x, int y, double xChange, double yChange) {
        this.x = (int)(x + radius * xChange);
        this.y = (int)(y + radius * yChange);
    }

    public Insect getInsect() { return this.insect; }

    @Override
    public void update(Graphics g) {
        for (IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }
}
