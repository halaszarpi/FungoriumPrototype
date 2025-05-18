package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class GSpore implements IObservable{
    Spore spore;
    int x;
    int y;
    private final int radius = 50;
    ArrayList<IObserver> observers;

    public GSpore(Spore spore) {
        this.spore = spore;
        x = -1;
        y = -1;
        observers = new ArrayList<>();
    }

    @Override
    public void attach(IObserver o) { observers.add(o); }

    @Override
    public void detach(IObserver o) { observers.remove(o); }

    @Override
    public void update(Graphics g) {
        for(IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }

    @Override
    public IObserver getObserver() { return observers.getFirst(); }

    public void setCoordinates(int x, int y, double xChange, double yChange) {
        this.x = (int)(x + radius * xChange);
        this.y = (int)(y + radius * yChange);
    }

    public Spore getSpore() { return spore; }

    public Point getCoords() { return new Point(x, y); }
}
