package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * This class is responsible for storing the coordinates of an insect and
 * attaching-detaching its observers and updating.
 */
public class GInsect implements IObservable {
    private Insect insect;
    private int x;
    private int y;
    private final int radius = 65;
    private ArrayList<IObserver> observers;

    /**
     * Constructor
     * @param i the insect
     */
    public GInsect(Insect i) {
        this.insect = i;
        this.x = 0;
        this.y = 0;
        observers = new ArrayList<>();
    }

    /**
     * Attachment of an observer.
     * @param o
     */
    public void attach(IObserver o) {
        observers.add(o);
    }

    /**
     * Detachment of an observer.
     * @param o
     */
    public void detach(IObserver o) {
        observers.remove(o);
    }

    /**
     * Returns the first observer of the observer list.
     */
    public IObserver getObserver() { return this.observers.get(0); }

    /**
     * Returns the coordinates of the insect.
     * @return the coordinates (Point)
     */
    public Point getCoords() { return new Point(x, y); }
    
    /**
     * itt az xChange es yChange parameterek ezek:
     * x = sin(insectPhase + pi/180)
     * y = cos(insectPhase + pi/180)
     */
    /**
     * Setting the coordinates of the insect.
     * @param x its tecton's x coordinate
     * @param y its tecton's y coordinate
     * @param xChange x coordinate of the insect corrected with a phase change relative to other insects on the tecton
     * @param yChange y coordinate of the insect corrected with a phase change relative to other insects on the tecton
     */
    public void setCoordinates(int x, int y, double xChange, double yChange) {
        this.x = (int)(x + radius * xChange);
        this.y = (int)(y + radius * yChange);
    }

    /**
     * Returns the insect;
     * @return the insect
     */
    public Insect getInsect() { return this.insect; }

    /**
     * Update which calls every observer's draw method, giving the Insect's coordinates in parameter.
     */
    @Override
    public void update(Graphics g) {
        for (IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }
}
