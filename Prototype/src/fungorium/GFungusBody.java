package fungorium;

import java.awt.*;
import java.util.ArrayList;

/**
 *  This class is responsible for storing the coordinates of the FungusBody
 *  and for attaching-detaching its observers and updating.
 */
public class GFungusBody implements IObservable {
    private FungusBody fungusbody;
    private int x;
    private int y;
    private ArrayList<IObserver> observers;

    /**
     * Constructor.
     * @param fb The FungusBody of the graphical FungusBody.
     */
    public GFungusBody(FungusBody fb) {
        fungusbody = fb;
        x = -1;
        y = -1;
        observers = new ArrayList<>();
    }

    /**
     * Attachment of observers.
     * @param o the observer to be attached.
     */
    @Override
    public void attach(IObserver o) { observers.add(o);}

    /**
     * Detachment of observers.
     * @param the observer to be detached.
     */
    @Override
    public void detach(IObserver o) { observers.remove(o); }

    /**
     * Update which calls every observer's draw method, giving the FungusBody coordinates in parameter.
     */
    @Override
    public void update(Graphics g) {
        for(IObserver o : observers) {
            o.draw(g, new Point(x, y));
        }
    }

    /**
     * Returns the first observer from the observers list.
     */
    @Override
    public IObserver getObserver() { return observers.get(0); }

    /**
     * Sets the coordinates of the FungusBody on the game window.
     * @param x amount of pixel steps on the x axis
     * @param y amount of pixel steps on the y axis
     */
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the FungusBody.
     * @return the FungusBody
     */
    public FungusBody getFungusbody() { return fungusbody; }
}
