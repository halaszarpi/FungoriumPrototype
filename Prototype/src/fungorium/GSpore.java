package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Class for Spore positioning and observers handling.
 */
public class GSpore implements IObservable {
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

    /**
     * Attachment of an observer.
     * @param o the observer
     */
    @Override
    public void attach(IObserver o) { observers.add(o); }

    /**
     * Detachment of an observer.
     * @param o the observer
     */
    @Override
    public void detach(IObserver o) { observers.remove(o); }

    /**
     * Update which calls every observer's draw method, giving the Spore's coordinates in parameter.
     */
    @Override
    public void update(Graphics g) {
        for(IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }

    /**
     * Returns the first observer from the observer list.
     */
    @Override
    public IObserver getObserver() { return observers.get(0); }

    /**
     * itt az xChange es yChange parameterek ezek:
     * x = sin(sporePhase + pi/180)
     * y = cos(sporePhase + pi/180)
     */
    /**
     * Setting the coordinates of the insect.
     * @param x its tecton's x coordinate
     * @param y its tecton's y coordinate
     * @param xChange x coordinate of the spore corrected with a phase change relative to other spores on the tecton
     * @param yChange y coordinate of the spore corrected with a phase change relative to other spores on the tecton
     */
    public void setCoordinates(int x, int y, double xChange, double yChange) {
        this.x = (int)(x + radius * xChange);
        this.y = (int)(y + radius * yChange);
    }

    /**
     * Returns the GSpore's spore.
     * @return the spore
     */
    public Spore getSpore() { return spore; }

    /**
     * Returns the coordinates of the spore.
     * @return the coordinates (Point)
     */
    public Point getCoords() { return new Point(x, y); }
}
