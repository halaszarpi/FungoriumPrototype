package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * This class is responsible for storing the coordinates of a mycelium and
 * attaching-detaching its observers and updating.
 */
public class GMycelium implements IObservable{
    private Mycelium mycelium;
    private int x;
    private int y;
    private final int radius = 25;
    private ArrayList<IObserver> observers;

    /**
     * Constructor
     * @param m the mycelium of the GMycelium
     */
    public GMycelium(Mycelium m){
        mycelium = m;
        x = -1;
        y = -1;
        observers = new ArrayList<>();
    }

    /**
     * Attachment of an observer.
     * @param o the observer
     */
    @Override
    public void attach(IObserver o) {
        observers.add(o);
    }

    
    /**
     * Detachment of an observer.
     * @param o observer
     */
    @Override
    public void detach(IObserver o) {
        observers.remove(o);
    }

    /**
     * Update which calls every observer's draw method, giving the Insect's coordinates in parameter.
     */
    @Override
    public void update(Graphics g) {
        for (IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }

    /**
     * itt az xChange es yChange parameterek ezek:
     * x = sin(myceliumPhase + pi/180)
     * y = cos(myceliumPhase + pi/180)
     */
    /**
     * Setting the coordinates of the insect.
     * @param x its tecton's x coordinate
     * @param y its tecton's y coordinate
     * @param xChange x coordinate of the mycelium corrected with a phase change relative to other myceliums on the tecton
     * @param yChange y coordinate of the mycelium corrected with a phase change relative to other myceliums on the tecton
     */
    public void setCoordinates(int x, int y, double xChange, double yChange) {
        this.x = (int)(x + radius * xChange);
        this.y = (int)(y + radius * yChange);
    }

    /**
     * Return the first observer from the observer list.
     * @return the observer
     */
    @Override
    public IObserver getObserver() { return observers.get(0); }

    /**
     * Returns the mycelium.
     * @return the mycelium
     */
    public Mycelium getMycelium() { return mycelium; }

    /**
     * Returns the coordinates of the mycelium.
     * @return the coordinates (Point)
     */
    public Point getCoords() { return new Point(x, y); }
}
