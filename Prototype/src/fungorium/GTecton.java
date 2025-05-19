package fungorium;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * This class is responsible for storing coordinates of a tecton and handling its observers.
 */
public class GTecton implements IObservable {
    Tecton tecton;
    GMap map;
    int x;
    int y;
    ArrayList<IObserver> observers;

    public GTecton(Tecton t, GMap m) {
        tecton = t;
        map = m; 
        x = -1;
        y = -1;
        // Újítás, minden osztályhoz kell egy getView(), ami visszaadja az osztály view-ját.
        observers = new ArrayList<>();
        attach(tecton.getView());
    }

    /**
     * Attachment of an observer.
     * @param o
     */
    @Override
    public void attach(IObserver o) {
        observers.add(o);
    }

    /**
     * Detachment of an observer.
     * @param o
     */
    @Override
    public void detach(IObserver o) {
        observers.remove(o);
    }

    /**
     * A helper class that connects phases with the number of objects.
     */
    private class ValuePair {
        private int phase;
        private int count;
        public ValuePair(int phase, int count) {
            this.phase = phase;
            this.count = count;
        }
        public int getPhase() { return this.phase; }
        public void setPhase(int phase) { this.phase = phase; }
        public int getCount() { return this.count; }
        public void increaseCount() { ++count; }
    }

    /**
     * Heart of the update methods.
     * Update which calls every observer's draw method, giving the Tecton's coordinates.
     * In case of different objects' views, there is a phase computation that helps the
     * placment of the matching objects in concentrical rounds, here lies the usage of the private class ValuePair. 
     */
    @Override
    public void update(Graphics g) {

        ValuePair insectVP = new ValuePair(0, 0);
        ValuePair myceliumVP = new ValuePair(0,0 );
        ValuePair sporeVP = new ValuePair(0, 0);

        // Megszamoljuk mibol mennyi van es annak megfeleloen lesznek szogekkel eltolva a koncentrikus korokben az elemek
        for (IObserver o : observers) {
            if (o instanceof MyceliumView) { myceliumVP.increaseCount();}
            else if (o instanceof SporeView) { sporeVP.increaseCount(); }
            else if (o instanceof InsectView) { insectVP.increaseCount(); }
        }

        if (insectVP.getCount() != 0) {
            insectVP.setPhase(360 / insectVP.getCount());
        } else {
            insectVP.setPhase(0);
        }

        if (myceliumVP.getCount() != 0) {
            myceliumVP.setPhase(360 / myceliumVP.getCount());
        } else {
            myceliumVP.setPhase(0);
        }

        if (sporeVP.getCount() != 0) {
            sporeVP.setPhase(360 / sporeVP.getCount());
        } else {
            sporeVP.setPhase(0);
        }

        int insectCurrentPhase = 0;
        int myceliumCurrentPhase = 0;
        int sporeCurrentPhase = 0;

        // Vegigmegyunk az observereken es mindegyiket a megfelelo pozicionalassal felrajzoljuk
        for (IObserver o : observers) {

            if (o instanceof MyceliumView mv) {
                double xChange = Math.sin(myceliumCurrentPhase * Math.PI / 180);
                double yChange = Math.cos(myceliumCurrentPhase * Math.PI / 180);

                mv.getMycelium().getGMycelium().setCoordinates(x, y, xChange, yChange);
                mv.getMycelium().getGMycelium().update(g);

                o.draw(g, mv.getMycelium().getGMycelium().getCoords());
                myceliumCurrentPhase += myceliumVP.getPhase();
            }

            else if (o instanceof InsectView iv) {
                double xChange = Math.sin(insectCurrentPhase * Math.PI / 180);
                double yChange = Math.cos(insectCurrentPhase * Math.PI / 180);

                iv.getInsect().getGInsect().setCoordinates(x, y, xChange, yChange);
                iv.getInsect().getGInsect().update(g);

                o.draw(g, iv.getInsect().getGInsect().getCoords());
                insectCurrentPhase += insectVP.getPhase();
            }

            else if (o instanceof SporeView sv)
            {
                double xChange = Math.sin(sporeCurrentPhase * Math.PI / 180);
                double yChange = Math.cos(sporeCurrentPhase * Math.PI / 180);

                sv.getSpore().getGSpore().setCoordinates(x, y, xChange, yChange);
                sv.getSpore().getGSpore().update(g);

                o.draw(g, sv.getSpore().getGSpore().getCoords());
                sporeCurrentPhase += sporeVP.getPhase();
            }

            else {
                o.draw(g, new Point(x, y));
            }
            
        }
    }

    /**
     * Sets the coordinates of the tecton. 
     * @param x number of pixels on the x axis
     * @param y number of pixels on the y axis
     */
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the first observer of the observer list.
     */
    @Override
    public IObserver getObserver() { return observers.get(0); }

    /**
     * Returns the GTecton's tecton
     * @return the tecton
     */
    public Tecton getTecton() { return tecton; }

    /**
     * Returns the coordinates of the tecton (Point)
     * @return coordinates (Point)
     */
    public Point getCoords() { return new Point(x, y); }

}
